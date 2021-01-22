package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.AbstractNumberConstraint;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Constraint that validates that a number typed field has a value greater than the specified min
 * value inclusive.
 */
public class MinConstraint extends AbstractNumberConstraint {

  private final long min;

  MinConstraint(long min) {
    this.min = min;
  }

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Number value) {
    if (value == null) {
      return true;
    } else if (value instanceof Long || value instanceof Integer || value instanceof Short) {
      return value.longValue() >= min;
    } else if (value instanceof Float || value instanceof Double) {
      return value.floatValue() >= min;
    } else {
      throw new UnsupportedOperationException("Unhandled value type: " + value.getClass());
    }
  }

  @Override
  public String getViolationErrorCode() {
    return "field.violations.Min";
  }

  @Override
  public Map<String, Object> getViolationMessageParams() {
    final Map<String, Object> params = new HashMap<>();
    params.put("min", min);
    return Collections.unmodifiableMap(params);
  }
}
