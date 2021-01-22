package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;

/** Constraint that validates that a message or enum typed field is set. */
public class IsSetConstraint implements FieldConstraint {

  private static final Set<Descriptors.FieldDescriptor.Type> SUPPORTED_TYPES =
      new HashSet<>(
          Arrays.asList(
              Descriptors.FieldDescriptor.Type.MESSAGE, Descriptors.FieldDescriptor.Type.ENUM));

  IsSetConstraint() {}

  @Override
  public boolean supportsField(Descriptors.FieldDescriptor fieldDescriptor) {
    return !fieldDescriptor.isRepeated() && SUPPORTED_TYPES.contains(fieldDescriptor.getType());
  }

  @Override
  public boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Object value) {
    return value != null;
  }

  @Override
  public String getViolationErrorCode() {
    return "field.violations.IsSet";
  }
}
