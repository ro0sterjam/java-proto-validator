package com.ro0sterware.protovalidator.conditions.impl;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.Duration;
import com.google.protobuf.FloatValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.Int64Value;
import com.google.protobuf.Message;
import com.google.protobuf.ProtocolMessageEnum;
import com.google.protobuf.StringValue;
import com.google.protobuf.Timestamp;
import com.ro0sterware.protovalidator.conditions.AbstractFieldCondition;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;

/** Condition that applies if the field is equal to the given value */
public class FieldIsEqualToCondition extends AbstractFieldCondition {

  private final Object value;

  FieldIsEqualToCondition(String field, @Nullable Object value) {
    super(field);
    this.value = value;
  }

  @Override
  protected boolean applies(Message message, @Nullable Object value) {
    if (this.value instanceof ProtocolMessageEnum) {
      return Objects.equals(((ProtocolMessageEnum) this.value).getValueDescriptor(), value);
    }
    return Objects.equals(coerced(value), this.value);
  }

  @Override
  protected boolean supportsField(Descriptors.FieldDescriptor fieldDescriptor) {
    return !fieldDescriptor.isRepeated() && !fieldDescriptor.isMapField();
  }

  @Override
  public String getConditionCode() {
    return "constraint.condition.FieldIsEqualTo";
  }

  @Override
  public Map<String, Object> getConditionMessageParams() {
    final Map<String, Object> params = new HashMap<>(super.getConditionMessageParams());
    params.put("value", value);
    return Collections.unmodifiableMap(params);
  }

  private static Object coerced(Object value) {
    if (value == null) {
      return null;
    } else if (value instanceof BoolValue) {
      return ((BoolValue) value).getValue();
    } else if (value instanceof DoubleValue) {
      return ((DoubleValue) value).getValue();
    } else if (value instanceof FloatValue) {
      return ((FloatValue) value).getValue();
    } else if (value instanceof Int32Value) {
      return ((Int32Value) value).getValue();
    } else if (value instanceof Int64Value) {
      return ((Int64Value) value).getValue();
    } else if (value instanceof StringValue) {
      return ((StringValue) value).getValue();
    } else if (value instanceof Timestamp) {
      final Timestamp timestamp = (Timestamp) value;
      return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    } else if (value instanceof Duration) {
      final Duration duration = (Duration) value;
      return java.time.Duration.ofSeconds(duration.getSeconds(), duration.getNanos());
    } else {
      return value;
    }
  }
}
