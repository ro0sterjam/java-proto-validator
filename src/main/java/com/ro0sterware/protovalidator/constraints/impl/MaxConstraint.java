package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.AbstractNumberConstraint;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Constraint that validates that a number typed field has a value less than the specified max value
 * inclusive.
 */
public class MaxConstraint extends AbstractNumberConstraint {

  private final long max;

  MaxConstraint(long max) {
    this.max = max;
  }

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Number value) {
    if (value == null) {
      return true;
    } else if (value instanceof Long || value instanceof Integer || value instanceof Short) {
      return value.longValue() <= max;
    } else if (value instanceof Float || value instanceof Double) {
      return value.floatValue() <= max;
    } else {
      throw new UnsupportedOperationException("Unhandled value type: " + value.getClass());
    }
  }

  @Override
  public String getViolationErrorCode() {
    return "field.violations.Max";
  }

  @Override
  public Map<String, Object> getViolationMessageParams() {
    final Map<String, Object> params = new HashMap<>();
    params.put("max", max);
    return Collections.unmodifiableMap(params);
  }
}
