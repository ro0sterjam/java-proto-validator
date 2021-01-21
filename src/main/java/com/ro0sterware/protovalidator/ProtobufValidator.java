package com.ro0sterware.protovalidator;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.impl.FieldConstraints;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

/** Validator for protobuf messages. */
public class ProtobufValidator {

  private final Map<Descriptors.Descriptor, MessageValidator> messageValidators;

  public ProtobufValidator(Map<Descriptors.Descriptor, MessageValidator> messageValidators) {
    this.messageValidators = Collections.unmodifiableMap(new HashMap<>(messageValidators));
  }

  /**
   * Validate given message and return list of message violations.
   *
   * @param message message to validate
   * @return list of message violations
   */
  public List<MessageViolation> validate(Message message) {
    return validate(message, null);
  }

  /**
   * Validate given message and return list of message violations with given base path
   *
   * @param message message to validate
   * @param path base path to use in the message violations
   * @return list of message violations
   */
  private List<MessageViolation> validate(Message message, @Nullable String path) {
    if (!messageValidators.containsKey(message.getDescriptorForType())) {
      return Collections.emptyList();
    }

    final MessageValidator messageValidator = messageValidators.get(message.getDescriptorForType());
    final List<MessageViolation> messageViolations =
        messageValidator.getMessageViolations(path, message);

    final Map<Descriptors.FieldDescriptor, Object> fields = message.getAllFields();

    // Get all nested message violations
    final List<MessageViolation> nestedMessageViolations =
        fields.keySet().stream()
            .filter(field -> field.getType() == Descriptors.FieldDescriptor.Type.MESSAGE)
            .filter(field -> shouldValidateNestedMessage(messageValidator, field))
            .map(
                field -> {
                  final Message nestedMessage = (Message) fields.get(field);
                  final String fieldName = field.getJsonName();
                  final String subPath = path == null ? fieldName : path + "." + fieldName;
                  return validate(nestedMessage, subPath);
                })
            .flatMap(List::stream)
            .collect(Collectors.toList());

    final List<MessageViolation> allViolations = new ArrayList<>(messageViolations);
    allViolations.addAll(nestedMessageViolations);
    return Collections.unmodifiableList(allViolations);
  }

  public static Builder createBuilder() {
    return new Builder();
  }

  private boolean shouldValidateNestedMessage(
      MessageValidator messageValidator, Descriptors.FieldDescriptor field) {
    // Returns true only if message field is marked with a valid constraint
    return messageValidator.getFieldConstraints(field).contains(FieldConstraints.VALID);
  }

  public static class Builder {

    private final Map<Descriptors.Descriptor, MessageValidator> messageValidators = new HashMap<>();

    private Builder() {}

    /**
     * Register a {@link MessageValidator} to this {@link ProtobufValidator}
     *
     * @param messageValidator validator to register
     */
    public Builder registerValidator(MessageValidator messageValidator) {
      messageValidators.put(messageValidator.getMessageDescriptor(), messageValidator);
      return this;
    }

    public ProtobufValidator build() {
      return new ProtobufValidator(messageValidators);
    }
  }
}
