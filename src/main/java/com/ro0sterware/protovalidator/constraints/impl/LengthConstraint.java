package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.AbstractStringConstraint;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Constraint that validates that a String typed field has a length between the specified min and
 * max inclusive.
 */
public class LengthConstraint extends AbstractStringConstraint {

  private final int min;
  private final int max;

  LengthConstraint(int min, int max) {
    this.min = min;
    this.max = max;
  }

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable String string) {
    return string == null || (string.length() >= min && string.length() <= max);
  }

  @Override
  public String getViolationErrorCode() {
    return "field.violations.Length";
  }

  @Override
  public Map<String, Object> getViolationMessageParams() {
    final Map<String, Object> params = new HashMap<>();
    params.put("min", min);
    params.put("max", max);
    return Collections.unmodifiableMap(params);
  }
}
