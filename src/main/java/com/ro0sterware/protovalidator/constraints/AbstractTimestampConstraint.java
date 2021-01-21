package com.ro0sterware.protovalidator.constraints;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.Timestamp;
import java.time.Instant;
import javax.annotation.Nullable;

/** Abstract constraint for {@link Timestamp} wrapper typed fields. */
public abstract class AbstractTimestampConstraint implements FieldConstraint {

  @Override
  public boolean supportsField(Descriptors.FieldDescriptor fieldDescriptor) {
    return fieldDescriptor.getType().equals(Descriptors.FieldDescriptor.Type.MESSAGE)
        && fieldDescriptor.getMessageType().equals(Timestamp.getDescriptor());
  }

  @Override
  public boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Object value) {
    if (value == null) {
      return isValid(message, fieldDescriptor, null);
    } else if (value instanceof Timestamp) {
      final Timestamp timestamp = (Timestamp) value;
      return isValid(
          message,
          fieldDescriptor,
          Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()));
    } else {
      throw new IllegalStateException("Unexpected value: " + value);
    }
  }

  protected abstract boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Instant value);
}
