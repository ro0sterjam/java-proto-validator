package com.ro0sterware.protovalidator.conditions.impl;

import com.ro0sterware.protovalidator.conditions.NotCondition;

/** Condition that applies if the field is not true */
public class FieldIsNotTrueCondition extends NotCondition {

  FieldIsNotTrueCondition(String field) {
    super(new FieldIsTrueCondition(field));
  }

  @Override
  public String getConditionCode() {
    return "constraint.condition.FieldIsNotTrue";
  }
}
