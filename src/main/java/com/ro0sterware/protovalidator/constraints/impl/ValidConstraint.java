package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import javax.annotation.Nullable;

/**
 * Marker constraint for a message typed field to indicate that the nested message's fields should
 * not be validated.
 */
public class ValidConstraint implements FieldConstraint {

  ValidConstraint() {}

  @Override
  public boolean supportsField(Descriptors.FieldDescriptor fieldDescriptor) {
    return fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.MESSAGE;
  }

  @Override
  public boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Object value) {
    // This validator is simply a "marker" validator and thus no real validation is performed
    return true;
  }

  @Override
  public String getViolationErrorCode() {
    // Should never ne called since this validator is simply a "marker" validator and thus is never
    // "invalid"
    throw new UnsupportedOperationException();
  }
}
