package com.ro0sterware.protovalidator;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.impl.FieldConstraints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nullable;

/** Validator for protobuf messages. */
public class ProtobufValidator {

  private final Map<Descriptors.Descriptor, MessageValidator> messageValidators;

  private ProtobufValidator(Map<Descriptors.Descriptor, MessageValidator> messageValidators) {
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
            .map(field -> getNestedViolations(field, fields.get(field), path))
            .flatMap(List::stream)
            .collect(Collectors.toList());

    final List<MessageViolation> allViolations = new ArrayList<>(messageViolations);
    allViolations.addAll(nestedMessageViolations);
    return Collections.unmodifiableList(allViolations);
  }

  private boolean shouldValidateNestedMessage(
      MessageValidator messageValidator, Descriptors.FieldDescriptor field) {
    // Returns true only if message field is marked with a valid constraint
    return messageValidator.getFieldConstraints(field).contains(FieldConstraints.VALID);
  }

  private List<MessageViolation> getNestedViolations(
      Descriptors.FieldDescriptor field, Object fieldValue, @Nullable String path) {
    if (fieldValue instanceof Message) {
      final Message nestedMessage = (Message) fieldValue;
      final String fieldName = field.getJsonName();
      final String subPath = path == null ? fieldName : path + "." + fieldName;
      return validate(nestedMessage, subPath);
    } else if (fieldValue instanceof List) {
      final List<?> nestedMessages = (List<?>) fieldValue;
      final String fieldName = field.getJsonName();
      return IntStream.range(0, nestedMessages.size())
          .mapToObj(
              i -> {
                final String subPath =
                    (path == null ? fieldName : path + "." + fieldName) + "[" + i + "]";
                final Message nestedMessage = (Message) nestedMessages.get(i);
                return validate(nestedMessage, subPath);
              })
          .flatMap(List::stream)
          .collect(Collectors.toList());
    } else {
      throw new IllegalStateException(
          "Expected only message or list of messages in getNestedViolation but received type: "
              + fieldValue.getClass());
    }
  }

  public static Builder createBuilder() {
    return new Builder();
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
