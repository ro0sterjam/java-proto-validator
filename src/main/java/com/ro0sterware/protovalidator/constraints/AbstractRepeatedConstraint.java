package com.ro0sterware.protovalidator.constraints;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import java.util.List;
import javax.annotation.Nullable;

/** Abstract constraint for repeated fields. */
public abstract class AbstractRepeatedConstraint implements FieldConstraint {

  @Override
  public boolean supportsField(Descriptors.FieldDescriptor fieldDescriptor) {
    return fieldDescriptor.isRepeated() && !fieldDescriptor.isMapField();
  }

  @Override
  public boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Object value) {
    // Repeated fields cannot be null
    if (value instanceof List<?>) {
      return isValid(message, fieldDescriptor, (List<?>) value);
    } else {
      throw new IllegalStateException("Unexpected value: " + value);
    }
  }

  protected abstract boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, List<?> list);
}
