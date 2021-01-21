package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.AbstractBooleanConstraint;
import javax.annotation.Nullable;

/** Constraint that validates that a boolean like field value is `false`. */
public class AssertTrueConstraint extends AbstractBooleanConstraint {

  AssertTrueConstraint() {}

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Boolean value) {
    // null values are valid
    return value == null || value;
  }

  @Override
  public String getViolationErrorCode() {
    return "field.violations.AssertTrue";
  }
}
