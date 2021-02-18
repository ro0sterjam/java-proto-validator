package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.AbstractRepeatedConstraint;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Constraint that validates that a repeated field has a size between the specified min and max
 * inclusive.
 */
public class SizeConstraint extends AbstractRepeatedConstraint {

  private final int min;
  private final int max;

  SizeConstraint(int min, int max) {
    this.min = min;
    this.max = max;
  }

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, List<?> value) {
    return value.size() >= min && value.size() <= max;
  }

  @Override
  public String getViolationErrorCode() {
    return "field.violations.Size";
  }

  @Override
  public Map<String, Object> getViolationMessageParams() {
    final Map<String, Object> params = new HashMap<>();
    params.put("min", min);
    params.put("max", max);
    return Collections.unmodifiableMap(params);
  }
}
