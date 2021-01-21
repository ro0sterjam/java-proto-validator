package com.ro0sterware.protovalidator.constraints;

import com.google.protobuf.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract constraint for number types and number wrapper message types. Does not support unsigned
 * number representations.
 */
public abstract class AbstractNumberConstraint extends NullSafeFieldConstraint {

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
  protected boolean nullSafeIsValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, Object value) {
    if (value instanceof Double) {
      return isValid(message, fieldDescriptor, (double) value);
    } else if (value instanceof Float) {
      return isValid(message, fieldDescriptor, (float) value);
    } else if (value instanceof Integer) {
      return isValid(message, fieldDescriptor, (int) value);
    } else if (value instanceof Long) {
      return isValid(message, fieldDescriptor, (long) value);
    } else if (value instanceof Short) {
      return isValid(message, fieldDescriptor, (short) value);
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
      Message message, Descriptors.FieldDescriptor fieldDescriptor, double value);

  protected abstract boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, float value);

  protected abstract boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, long value);

  protected abstract boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, int value);

  protected abstract boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, short value);
}
