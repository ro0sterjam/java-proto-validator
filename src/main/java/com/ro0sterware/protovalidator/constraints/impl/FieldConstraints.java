package com.ro0sterware.protovalidator.constraints.impl;

import com.ro0sterware.protovalidator.constraints.FieldConstraint;

public class FieldConstraints {

  /** Validates that a field is set */
  public static final FieldConstraint IS_SET = new IsSetConstraint();

  /** Validates that a field is not set */
  public static final FieldConstraint IS_NOT_SET = new IsNotSetConstraint();

  /** Marks field to validate nested fields */
  public static final FieldConstraint VALID = new ValidConstraint();

  /** Validates that a number field is positive */
  public static final FieldConstraint POSITIVE = new PositiveConstraint();

  /** Validates that a number field is negative */
  public static final FieldConstraint NEGATIVE = new NegativeConstraint();

  /** Validates that a number field is positive or zero */
  public static final FieldConstraint POSITIVE_OR_ZERO = new PositiveOrZeroConstraint();

  /** Validates that a number field is negative or zero */
  public static final FieldConstraint NEGATIVE_OR_ZERO = new NegativeOrZeroConstraint();

  /** Validates that a string field is not null or blank */
  public static final FieldConstraint NOT_BLANK = new NotBlankConstraint();

  /** Validates that a repeated field is not null or empty */
  public static final FieldConstraint NOT_EMPTY = new NotEmptyConstraint();

  /** Validates that a boolean field is true */
  public static final FieldConstraint ASSERT_TRUE = new AssertTrueConstraint();

  /** Validates that a boolean field is false */
  public static final FieldConstraint ASSERT_FALSE = new AssertFalseConstraint();

  /** Validates that a timestamp field is in the future */
  public static final FieldConstraint FUTURE = new FutureConstraint();

  /** Validates that a timestamp field is in the past */
  public static final FieldConstraint PAST = new PastConstraint();

  /** Validates that a string field has a length within min and max inclusive */
  public static FieldConstraint length(int min, int max) {
    return new LengthConstraint(min, max);
  }

  /** Validates that a number field is less than or equal to max */
  public static FieldConstraint max(int max) {
    return new MaxConstraint(max);
  }

  /** Validates that a number field is greater than or equal to min */
  public static FieldConstraint min(int min) {
    return new MinConstraint(min);
  }

  /** Validates that a string field matches the given regular expression */
  public static FieldConstraint pattern(String regexp) {
    return new PatternConstraint(regexp);
  }

  /** Validates that a repeated field has a size within min and max inclusive */
  public static FieldConstraint size(int min, int max) {
    return new SizeConstraint(min, max);
  }

  /** Validates that a map field has a key */
  public static FieldConstraint hasKey(Object key) {
    return new HasKeyConstraint(key);
  }
}
