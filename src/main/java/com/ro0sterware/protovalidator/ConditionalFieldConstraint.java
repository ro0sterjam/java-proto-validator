package com.ro0sterware.protovalidator;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.conditions.ApplyCondition;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import javax.annotation.Nullable;

/**
 * Internal {@link FieldConstraint} that applies only if the supplied {@link ApplyCondition}
 * evaluates to true for the evaluating message.
 */
public class ConditionalFieldConstraint implements FieldConstraint {

  private final FieldConstraint fieldConstraint;
  private final ApplyCondition condition;

  ConditionalFieldConstraint(FieldConstraint fieldConstraint, ApplyCondition condition) {
    this.fieldConstraint = Objects.requireNonNull(fieldConstraint);
    this.condition = Objects.requireNonNull(condition);
  }

  @Override
  public boolean supportsField(Descriptors.FieldDescriptor fieldDescriptor) {
    return fieldConstraint.supportsField(fieldDescriptor);
  }

  @Override
  public boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Object value) {
    return !condition.applies(message) || fieldConstraint.isValid(message, fieldDescriptor, value);
  }

  @Override
  public String getViolationErrorCode() {
    return fieldConstraint.getViolationErrorCode();
  }

  @Override
  public Map<String, Object> getViolationMessageParams() {
    return fieldConstraint.getViolationMessageParams();
  }

  String getConditionCode() {
    return condition.getConditionCode();
  }

  Map<String, Object> getConditionMessageParams() {
    return condition.getConditionMessageParams();
  }

  /**
   * Return the constraint's error message concatenated with the condition's message.
   *
   * @return the error message concatenated with the condtion message
   */
  @Nullable
  public String getDefaultErrorMessage() {
    final String errorMessage = fieldConstraint.getDefaultErrorMessage();
    final String conditionMessage = condition.getDefaultConditionMessage();
    return new StringJoiner(" ").add(errorMessage).add(conditionMessage).toString();
  }
}
