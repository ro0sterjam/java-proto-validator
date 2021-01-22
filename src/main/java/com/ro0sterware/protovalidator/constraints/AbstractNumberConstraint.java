package com.ro0sterware.protovalidator.constraints;

import com.google.protobuf.Descriptors;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.FloatValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.Int64Value;
import com.google.protobuf.Message;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;

/**
 * Abstract constraint for number types and number wrapper message types. Does not support unsigned
 * number representations.
 */
public abstract class AbstractNumberConstraint implements FieldConstraint {

  private static final Set<Descriptors.FieldDescriptor.Type> SUPPORTED_TYPES =
      new HashSet<>(
          Arrays.asList(
              Descriptors.FieldDescriptor.Type.DOUBLE,
              Descriptors.FieldDescriptor.Type.FLOAT,
              Descriptors.FieldDescriptor.Type.INT32,
              Descriptors.FieldDescriptor.Type.INT64,
              Descriptors.FieldDescriptor.Type.SFIXED32,
              Descriptors.FieldDescriptor.Type.SFIXED64,
              Descriptors.FieldDescriptor.Type.SINT32,
              Descriptors.FieldDescriptor.Type.SINT64));
  private static final Set<Descriptors.Descriptor> SUPPORTED_MESSAGE_TYPES =
      new HashSet<>(
          Arrays.asList(
              DoubleValue.getDescriptor(),
              FloatValue.getDescriptor(),
              Int32Value.getDescriptor(),
              Int64Value.getDescriptor()));

  @Override
  public boolean supportsField(Descriptors.FieldDescriptor fieldDescriptor) {
    return SUPPORTED_TYPES.contains(fieldDescriptor.getType())
        || (fieldDescriptor.getType().equals(Descriptors.FieldDescriptor.Type.MESSAGE)
            && SUPPORTED_MESSAGE_TYPES.contains(fieldDescriptor.getMessageType()));
  }

  @Override
  public boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Object value) {
    if (value == null || value instanceof Number) {
      return isValid(message, fieldDescriptor, (Number) value);
    } else if (value instanceof DoubleValue) {
      return isValid(message, fieldDescriptor, ((DoubleValue) value).getValue());
    } else if (value instanceof FloatValue) {
      return isValid(message, fieldDescriptor, ((FloatValue) value).getValue());
    } else if (value instanceof Int32Value) {
      return isValid(message, fieldDescriptor, ((Int32Value) value).getValue());
    } else if (value instanceof Int64Value) {
      return isValid(message, fieldDescriptor, ((Int64Value) value).getValue());
    }
    throw new IllegalStateException("Unexpected value: " + value);
  }

  protected abstract boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Number value);
}
