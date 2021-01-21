package com.ro0sterware.protovalidator;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.conditions.ApplyCondition;
import com.ro0sterware.protovalidator.constraints.ConditionalFieldConstraint;
import com.ro0sterware.protovalidator.constraints.Constraint;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import com.ro0sterware.protovalidator.constraints.MessageConstraint;
import com.ro0sterware.protovalidator.exceptions.ApplyConditionNotSupportedException;
import com.ro0sterware.protovalidator.exceptions.FieldConstraintNotSupportedException;
import com.ro0sterware.protovalidator.exceptions.FieldDoesNotExistException;
import com.ro0sterware.protovalidator.exceptions.MessageConstraintNotSupportedException;
import com.ro0sterware.protovalidator.utils.ProtoFieldUtils;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;

/** Validator for a message type. */
public class MessageValidator {

  private static final String DEFAULT_ERROR_MESSAGE = "is invalid";

  private final Descriptors.Descriptor messageDescriptor;
  private final List<MessageConstraint> messageConstraints;
  private final Map<Descriptors.FieldDescriptor, List<FieldConstraint>> fieldConstraints;

  private MessageValidator(
      Descriptors.Descriptor messageDescriptor,
      List<MessageConstraint> messageConstraints,
      Map<Descriptors.FieldDescriptor, List<FieldConstraint>> fieldConstraints) {
    this.messageDescriptor = Objects.requireNonNull(messageDescriptor);
    this.messageConstraints = Collections.unmodifiableList(new ArrayList<>(messageConstraints));
    this.fieldConstraints = Collections.unmodifiableMap(new HashMap<>(fieldConstraints));
  }

  Descriptors.Descriptor getMessageDescriptor() {
    return messageDescriptor;
  }

  /**
   * Get all the message violations for the given message under the given path
   *
   * @param path dot delimited path to add the violations under
   * @param message message to get violations for
   * @return constraint violations for this message
   */
  public List<MessageViolation> getMessageViolations(@Nullable String path, Message message) {
    assertCorrectMessageType(message);

    // Get all message level violations
    final Stream<MessageViolation> messageViolations =
        messageConstraints.stream()
            .filter(constraint -> !constraint.isValid(message))
            .map(constraint -> createMessageViolation(constraint, path, message));

    // Get all field level violations
    final Stream<MessageViolation> fieldViolations =
        fieldConstraints.keySet().stream()
            .flatMap(
                fieldDescriptor -> {
                  final String field = fieldDescriptor.getJsonName();
                  final Object value = getValue(message, fieldDescriptor);
                  final String fieldPath = path == null ? field : path + "." + field;
                  return fieldConstraints.get(fieldDescriptor).stream()
                      .filter(constraint -> !constraint.isValid(message, fieldDescriptor, value))
                      .map(constraint -> createMessageViolation(constraint, fieldPath, value));
                });

    return Stream.concat(messageViolations, fieldViolations).collect(Collectors.toList());
  }

  /**
   * Return list of all field constraints on the given fieldDescriptor.
   *
   * @param fieldDescriptor field descriptor in which to retrieve the field constraints for
   * @return all the field constraints on the given fieldDescriptor
   */
  public List<FieldConstraint> getFieldConstraints(Descriptors.FieldDescriptor fieldDescriptor) {
    return fieldConstraints.getOrDefault(fieldDescriptor, Collections.emptyList());
  }

  private MessageViolation createMessageViolation(
      Constraint constraint, @Nullable String path, @Nullable Object value) {
    final String nullSafePath = path == null ? "" : path;
    final MessageViolation.ErrorMessage error = constructErrorMessage(constraint);
    final MessageViolation.ConditionMessage condition = constructConditionMessage(constraint);
    final String defaultErrorMessage = constructDefaultErrorMessage(constraint);
    return new MessageViolation(nullSafePath, error, condition, defaultErrorMessage, value);
  }

  private MessageViolation.ErrorMessage constructErrorMessage(Constraint constraint) {
    return new MessageViolation.ErrorMessage(
        constraint.getViolationErrorCode(), constraint.getViolationMessageParams());
  }

  @Nullable
  private MessageViolation.ConditionMessage constructConditionMessage(Constraint constraint) {
    if (constraint instanceof ConditionalFieldConstraint) {
      final ConditionalFieldConstraint conditionalFieldConstraint =
          (ConditionalFieldConstraint) constraint;
      return new MessageViolation.ConditionMessage(
          conditionalFieldConstraint.getConditionCode(),
          conditionalFieldConstraint.getConditionMessageParams());
    } else {
      return null;
    }
  }

  private String constructDefaultErrorMessage(Constraint constraint) {
    return constraint.getDefaultErrorMessage() == null
        ? DEFAULT_ERROR_MESSAGE
        : constraint.getDefaultErrorMessage();
  }

  @Nullable
  private Object getValue(Message message, Descriptors.FieldDescriptor fieldDescriptor) {
    if (fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.MESSAGE
        || fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.ENUM) {
      // getField for message and enum just returns the default value even if not set, thus we
      // need
      // to explicitly check if the field is set
      return message.hasField(fieldDescriptor) ? message.getField(fieldDescriptor) : null;
    }
    return message.getField(fieldDescriptor);
  }

  private void assertCorrectMessageType(Message message) {
    if (!messageDescriptor.equals(message.getDescriptorForType())) {
      throw new IllegalArgumentException(
          String.format(
              "Expected message with type: %s, instead received %s",
              messageDescriptor.getFullName(), message.getDescriptorForType().getFullName()));
    }
  }

  public static Builder createBuilder(Descriptors.Descriptor messageDescriptor) {
    return new Builder(messageDescriptor);
  }

  public static class Builder {

    private final Descriptors.Descriptor messageDescriptor;
    private final List<MessageConstraint> messageConstraints;
    private final Map<Descriptors.FieldDescriptor, List<FieldConstraint>> fieldConstraints;

    private Builder(Descriptors.Descriptor messageDescriptor) {
      this.messageDescriptor = Objects.requireNonNull(messageDescriptor);
      this.messageConstraints = new ArrayList<>();
      this.fieldConstraints = new HashMap<>();
    }

    /**
     * Add MessageConstraint to this MessageValidator.
     *
     * @param messageConstraint constraint to add
     * @return this
     */
    public Builder addMessageConstraint(MessageConstraint messageConstraint) {
      if (!messageConstraint.supportsMessage(messageDescriptor)) {
        throw new MessageConstraintNotSupportedException(messageConstraint, messageDescriptor);
      }
      messageConstraints.add(messageConstraint);
      return this;
    }

    /**
     * Add FieldConstraint to this MessageValidator.
     *
     * @param field field to add constraint for
     * @param fieldConstraint constraint to add
     * @return this
     */
    public Builder addFieldConstraint(String field, FieldConstraint fieldConstraint) {
      final String protoFieldName = ProtoFieldUtils.toLowerSnakeCase(field);
      final Descriptors.FieldDescriptor fieldDescriptor =
          messageDescriptor.findFieldByName(protoFieldName);
      if (fieldDescriptor == null) {
        throw new FieldDoesNotExistException(messageDescriptor, field);
      } else if (!fieldConstraint.supportsField(fieldDescriptor)) {
        throw new FieldConstraintNotSupportedException(fieldConstraint, fieldDescriptor);
      }
      fieldConstraints
          .computeIfAbsent(fieldDescriptor, key -> new ArrayList<>())
          .add(fieldConstraint);
      return this;
    }

    /**
     * Add FieldConstraint with to this MessageValidator that only applies if the condition
     * evaluates to true.
     *
     * @param field field to add constraint for
     * @param fieldConstraint constraint to add
     * @param condition condition to add to this constraint
     * @return this
     */
    public Builder addFieldConstraint(
        String field, FieldConstraint fieldConstraint, ApplyCondition condition) {
      if (!condition.supportsMessage(messageDescriptor)) {
        throw new ApplyConditionNotSupportedException(condition, messageDescriptor);
      }
      return addFieldConstraint(field, new ConditionalFieldConstraint(fieldConstraint, condition));
    }

    public MessageValidator build() {
      return new MessageValidator(messageDescriptor, messageConstraints, fieldConstraints);
    }
  }
}
