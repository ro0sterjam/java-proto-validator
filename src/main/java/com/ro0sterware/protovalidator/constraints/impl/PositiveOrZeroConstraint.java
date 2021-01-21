package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.AbstractNumberConstraint;

/** Constraint that validates that a number typed field is positive or zero. */
public class PositiveOrZeroConstraint extends AbstractNumberConstraint {

  PositiveOrZeroConstraint() {}

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, double value) {
    return value >= 0.0;
  }

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, float value) {
    return value >= 0.0;
  }

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, long value) {
    return value >= 0L;
  }

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, int value) {
    return value >= 0;
  }

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, short value) {
    return value >= 0;
  }

  @Override
  public String getViolationErrorCode() {
    return "field.violations.PositiveOrZero";
  }
}
