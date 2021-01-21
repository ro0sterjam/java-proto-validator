package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.AbstractCollectionConstraint;
import java.util.Collection;
import javax.annotation.Nullable;

/** Constraint that validates that a repeated or map typed field is neither null nor empty. */
public class NotEmptyConstraint extends AbstractCollectionConstraint {

  NotEmptyConstraint() {}

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Collection<?> value) {
    return value != null && !value.isEmpty();
  }

  @Override
  public String getViolationErrorCode() {
    return "field.violations.NotEmpty";
  }
}
