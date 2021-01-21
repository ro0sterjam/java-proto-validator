package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.AbstractNumberConstraint;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
      Message message, Descriptors.FieldDescriptor fieldDescriptor, double value) {
    return value >= min;
  }

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, float value) {
    return value >= min;
  }

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, long value) {
    return value >= min;
  }

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, int value) {
    return value >= min;
  }

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, short value) {
    return value >= min;
  }

  @Override
  public String getViolationErrorCode() {
    return "field.violations.Min";
  }

  @Override
  public Map<String, Object> getViolationMessageParams() {
    final HashMap<String, Object> params = new HashMap<>();
    params.put("min", min);
    return Collections.unmodifiableMap(params);
  }
}
