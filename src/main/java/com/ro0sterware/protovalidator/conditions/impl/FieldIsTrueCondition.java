package com.ro0sterware.protovalidator.conditions.impl;

import com.ro0sterware.protovalidator.conditions.AbstractBooleanFieldCondition;
import javax.annotation.Nullable;

/** Condition that applies if the field is true */
public class FieldIsTrueCondition extends AbstractBooleanFieldCondition {

  FieldIsTrueCondition(String field) {
    super(field);
  }

  @Override
  protected boolean applies(@Nullable Boolean bool) {
    return Boolean.TRUE.equals(bool);
  }

  @Override
  public String getConditionCode() {
    return "constraint.condition.FieldIsTrue";
  }
}
