package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.AbstractBooleanConstraint;

/** Constraint that validates that a boolean field value is `false`. */
public class AssertTrueConstraint extends AbstractBooleanConstraint {

  AssertTrueConstraint() {}

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, boolean value) {
    return value;
  }

  @Override
  public String getViolationErrorCode() {
    return "field.violations.AssertTrue";
  }
}
