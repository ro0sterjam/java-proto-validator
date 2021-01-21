package com.ro0sterware.protovalidator.constraints;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import java.util.Collection;
import javax.annotation.Nullable;

/** Abstract constraint for repeated and map fields. */
public abstract class AbstractCollectionConstraint implements FieldConstraint {

  @Override
  public boolean supportsField(Descriptors.FieldDescriptor fieldDescriptor) {
    return fieldDescriptor.isRepeated() || fieldDescriptor.isMapField();
  }

  @Override
  public boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Object value) {
    if (value == null) {
      return isValid(message, fieldDescriptor, null);
    } else if (value instanceof Collection<?>) {
      return isValid(message, fieldDescriptor, (Collection<?>) value);
    } else {
      throw new IllegalStateException("Unexpected value: " + value);
    }
  }

  protected abstract boolean isValid(
      Message message,
      Descriptors.FieldDescriptor fieldDescriptor,
      @Nullable Collection<?> collection);
}
