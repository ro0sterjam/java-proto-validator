package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.AbstractStringConstraint;
import javax.annotation.Nullable;

/** Constraint that validates that a String typed field is neither null nor blank. */
public class NotBlankConstraint extends AbstractStringConstraint {

  NotBlankConstraint() {}

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable String value) {
    return value != null && value.trim().length() > 0;
  }

  @Override
  public String getViolationErrorCode() {
    return "field.violations.NotBlank";
  }
}
