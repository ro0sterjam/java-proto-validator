package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.AbstractNumberConstraint;
import javax.annotation.Nullable;

/** Constraint that validates that a number typed field is negative. */
public class NegativeConstraint extends AbstractNumberConstraint {

  NegativeConstraint() {}

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Number value) {
    if (value == null) {
      return true;
    } else if (value instanceof Long || value instanceof Integer || value instanceof Short) {
      return value.longValue() < 0;
    } else if (value instanceof Float || value instanceof Double) {
      return value.floatValue() < 0;
    } else {
      throw new UnsupportedOperationException("Unhandled value type: " + value.getClass());
    }
  }

  @Override
  public String getViolationErrorCode() {
    return "field.violations.Negative";
  }
}
