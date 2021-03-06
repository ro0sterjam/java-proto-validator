package com.ro0sterware.protovalidator.conditions.impl;

import com.ro0sterware.protovalidator.conditions.ApplyCondition;
import javax.annotation.Nullable;

public class ApplyConditions {

  private ApplyConditions() {}

  /** Syntactic sugar for ApplyCondition */
  public static ApplyCondition when(ApplyCondition condition) {
    return condition;
  }

  /** Condition that applies if the SpEL expression evaluates to true for the message */
  public static ApplyCondition expression(String expression) {
    return new ExpressionCondition(expression);
  }

  /** Condition that applies if the field is true */
  public static ApplyCondition fieldIsTrue(String field) {
    return new FieldIsTrueCondition(field);
  }

  /** Condition that applies if the field is not true */
  public static ApplyCondition fieldIsNotTrue(String field) {
    return new FieldIsNotTrueCondition(field);
  }

  /** Condition that applies if the field is equal to the given value */
  public static ApplyCondition fieldIsEqualTo(String field, @Nullable Object value) {
    return new FieldIsEqualToCondition(field, value);
  }
}
