package com.ro0sterware.protovalidator.constraints;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import javax.annotation.Nullable;

/**
 * Abstract constraint for {@link Descriptors.FieldDescriptor.Type#BOOL} and {@link BoolValue}
 * wrapper typed fields.
 */
public abstract class AbstractBooleanConstraint implements FieldConstraint {

  @Override
  public boolean supportsField(Descriptors.FieldDescriptor fieldDescriptor) {
    return fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.BOOL
        || (fieldDescriptor.getType().equals(Descriptors.FieldDescriptor.Type.MESSAGE)
            && fieldDescriptor.getMessageType().equals(BoolValue.getDescriptor()));
  }

  @Override
  public boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Object value) {
    if (value == null || value instanceof Boolean) {
      return isValid(message, fieldDescriptor, (Boolean) value);
    } else if (value instanceof BoolValue) {
      return isValid(message, fieldDescriptor, ((BoolValue) value).getValue());
    } else {
      throw new IllegalStateException("Unexpected value: " + value);
    }
  }

  protected abstract boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Boolean value);
}
