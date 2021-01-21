package com.ro0sterware.protovalidator.constraints;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.StringValue;
import javax.annotation.Nullable;

/**
 * Abstract constraint for {@link Descriptors.FieldDescriptor.Type#STRING} and {@link StringValue}
 * wrapper typed fields.
 */
public abstract class AbstractStringConstraint implements FieldConstraint {

  @Override
  public boolean supportsField(Descriptors.FieldDescriptor fieldDescriptor) {
    return (fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.STRING
            || (fieldDescriptor.getType().equals(Descriptors.FieldDescriptor.Type.MESSAGE)
                && fieldDescriptor.getMessageType().equals(StringValue.getDescriptor())))
        && !fieldDescriptor.isRepeated();
  }

  @Override
  public boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Object value) {
    if (value == null) {
      return isValid(message, fieldDescriptor, null);
    } else if (value instanceof String) {
      return isValid(message, fieldDescriptor, (String) value);
    } else if (value instanceof StringValue) {
      return isValid(message, fieldDescriptor, ((StringValue) value).getValue());
    } else {
      throw new IllegalStateException("Unexpected value: " + value);
    }
  }

  protected abstract boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable String value);
}
