package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.AbstractCollectionConstraint;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Constraint that validates that a repeated or map field has a size between the specified min and
 * max inclusive.
 */
public class SizeConstraint extends AbstractCollectionConstraint {

  private final int min;
  private final int max;

  SizeConstraint(int min, int max) {
    this.min = min;
    this.max = max;
  }

  @Override
  protected boolean isValid(
      Message message,
      Descriptors.FieldDescriptor fieldDescriptor,
      @Nullable Collection<?> collection) {
    return collection == null || (collection.size() >= min && collection.size() <= max);
  }

  @Override
  public String getViolationErrorCode() {
    return "field.violations.Size";
  }

  @Override
  public Map<String, Object> getViolationMessageParams() {
    final HashMap<String, Object> params = new HashMap<>();
    params.put("min", min);
    params.put("max", max);
    return Collections.unmodifiableMap(params);
  }
}
