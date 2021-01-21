package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.AbstractNumberConstraint;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
      Message message, Descriptors.FieldDescriptor fieldDescriptor, double value) {
    return value <= max;
  }

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, float value) {
    return value <= max;
  }

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, long value) {
    return value <= max;
  }

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, int value) {
    return value <= max;
  }

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, short value) {
    return value <= max;
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
