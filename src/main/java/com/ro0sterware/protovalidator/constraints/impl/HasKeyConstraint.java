package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.AbstractMapConstraint;
import java.util.Collections;
import java.util.Map;

public class HasKeyConstraint extends AbstractMapConstraint {

  private final Object key;

  HasKeyConstraint(Object key) {
    this.key = key;
  }

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, Map<?, ?> map) {
    return map.containsKey(key);
  }

  @Override
  public String getViolationErrorCode() {
    return "field.violations.HasKey";
  }

  @Override
  public Map<String, Object> getViolationMessageParams() {
    return Collections.singletonMap("key", key);
  }
}
