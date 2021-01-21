package com.ro0sterware.protovalidator.constraints;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;

/**
 * Abstract constraint for {@link Descriptors.FieldDescriptor.Type#BOOL} and {@link BoolValue}
 * wrapper typed fields.
 *
 * <p>Expends {@link NullSafeFieldConstraint} and therefore is "Valid" even if value is `null`.
 */
public abstract class AbstractBooleanConstraint extends NullSafeFieldConstraint {

  @Override
  public boolean supportsField(Descriptors.FieldDescriptor fieldDescriptor) {
    return fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.BOOL
        || (fieldDescriptor.getType().equals(Descriptors.FieldDescriptor.Type.MESSAGE)
            && fieldDescriptor.getMessageType().equals(BoolValue.getDescriptor()));
  }

  @Override
  protected boolean nullSafeIsValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, Object value) {
    if (value instanceof Boolean) {
      return isValid(message, fieldDescriptor, (boolean) value);
    } else if (value instanceof BoolValue) {
      return isValid(message, fieldDescriptor, ((BoolValue) value).getValue());
    } else {
      throw new IllegalStateException("Unexpected value: " + value);
    }
  }

  protected abstract boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, boolean value);
}
