package com.ro0sterware.protovalidator.constraints;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import java.util.Map;
import javax.annotation.Nullable;

/** Abstract constraint for map fields. */
public abstract class AbstractMapConstraint implements FieldConstraint {

  @Override
  public boolean supportsField(Descriptors.FieldDescriptor fieldDescriptor) {
    return fieldDescriptor.isMapField();
  }

  @Override
  public boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Object value) {
    // Map fields cannot have a null value
    if (value instanceof Map) {
      return isValid(message, fieldDescriptor, (Map<?, ?>) value);
    } else {
      throw new IllegalStateException("Unexpected value: " + value);
    }
  }

  protected abstract boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, Map<?, ?> value);
}
