package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.AbstractRepeatedConstraint;
import java.util.List;

/** Constraint that validates that a repeated field is not empty. */
public class NotEmptyConstraint extends AbstractRepeatedConstraint {

  NotEmptyConstraint() {}

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, List<?> value) {
    return !value.isEmpty();
  }

  @Override
  public String getViolationErrorCode() {
    return "field.violations.NotEmpty";
  }
}
