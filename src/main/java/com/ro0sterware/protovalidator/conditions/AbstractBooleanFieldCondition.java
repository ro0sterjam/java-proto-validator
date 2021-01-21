package com.ro0sterware.protovalidator.conditions;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import javax.annotation.Nullable;

/** Abstract {@link ApplyCondition} for a boolean field type */
public abstract class AbstractBooleanFieldCondition extends AbstractFieldCondition {

  protected AbstractBooleanFieldCondition(String field) {
    super(field);
  }

  @Override
  protected boolean supportsField(Descriptors.FieldDescriptor fieldDescriptor) {
    return fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.BOOL
        || (fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.MESSAGE
            || fieldDescriptor.getMessageType().equals(BoolValue.getDescriptor()));
  }

  @Override
  protected boolean applies(Message message, @Nullable Object value) {
    if (value == null) {
      return applies((Boolean) null);
    } else if (value instanceof Boolean) {
      return applies((Boolean) value);
    } else if (value instanceof BoolValue) {
      return applies(((BoolValue) value).getValue());
    } else {
      throw new IllegalStateException("Unexpected value: " + value);
    }
  }

  protected abstract boolean applies(@Nullable Boolean bool);
}
