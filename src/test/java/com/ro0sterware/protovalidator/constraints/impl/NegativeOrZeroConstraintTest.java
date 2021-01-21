package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.*;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class NegativeOrZeroConstraintTest extends AbstractFieldConstraintTest {

  @Override
  FieldConstraint getTestFieldConstraint() {
    return FieldConstraints.NEGATIVE_OR_ZERO;
  }

  @Override
  Message getTestMessage() {
    return TestMessageOuterClass.TestMessage.getDefaultInstance();
  }

  @Override
  String[] getSupportedFields() {
    return new String[] {
      "doubleField",
      "floatField",
      "int32Field",
      "int64Field",
      "sint32Field",
      "sint64Field",
      "sfixed32Field",
      "sfixed64Field",
      "secondOneofField",
      "doubleWrapperField",
      "floatWrapperField",
      "int64WrapperField",
      "int32WrapperField"
    };
  }

  @Override
  Stream<Arguments> provideValidFieldValues() {
    return Stream.of(
        Arguments.of("doubleField", -0.1),
        Arguments.of("floatField", -0.1f),
        Arguments.of("int32Field", -1),
        Arguments.of("int64Field", -1L),
        Arguments.of("sint32Field", -1),
        Arguments.of("sint64Field", -1L),
        Arguments.of("sfixed32Field", -1),
        Arguments.of("sfixed64Field", -1L),
        Arguments.of("secondOneofField", -1),
        Arguments.of("doubleWrapperField", DoubleValue.of(-0.1)),
        Arguments.of("doubleWrapperField", null),
        Arguments.of("floatWrapperField", FloatValue.of(-0.1f)),
        Arguments.of("floatWrapperField", null),
        Arguments.of("int64WrapperField", Int64Value.of(-1L)),
        Arguments.of("int64WrapperField", null),
        Arguments.of("int32WrapperField", Int32Value.of(-1)),
        Arguments.of("int32WrapperField", null),
        Arguments.of("doubleField", 0.0),
        Arguments.of("floatField", 0.0f),
        Arguments.of("int32Field", 0),
        Arguments.of("int64Field", 0L),
        Arguments.of("sint32Field", 0),
        Arguments.of("sint64Field", 0L),
        Arguments.of("sfixed32Field", 0),
        Arguments.of("sfixed64Field", 0L),
        Arguments.of("secondOneofField", 0),
        Arguments.of("doubleWrapperField", DoubleValue.of(0.0)),
        Arguments.of("floatWrapperField", FloatValue.of(0.0f)),
        Arguments.of("int64WrapperField", Int64Value.of(0L)),
        Arguments.of("int32WrapperField", Int32Value.of(0)));
  }

  @Override
  Stream<Arguments> provideInvalidFieldValues() {
    return Stream.of(
        Arguments.of("doubleField", 0.1),
        Arguments.of("floatField", 0.1f),
        Arguments.of("int32Field", 1),
        Arguments.of("int64Field", 1L),
        Arguments.of("sint32Field", 1),
        Arguments.of("sint64Field", 1L),
        Arguments.of("sfixed32Field", 1),
        Arguments.of("sfixed64Field", 1L),
        Arguments.of("secondOneofField", 1),
        Arguments.of("doubleWrapperField", DoubleValue.of(0.1)),
        Arguments.of("floatWrapperField", FloatValue.of(0.1f)),
        Arguments.of("int64WrapperField", Int64Value.of(1L)),
        Arguments.of("int32WrapperField", Int32Value.of(1)));
  }

  @Override
  MessageViolation getExpectedMessageViolation(String field, Object value) {
    return new MessageViolation(
        field,
        new MessageViolation.ErrorMessage(
            "field.violations.NegativeOrZero", Collections.emptyMap()),
        null,
        "must be less than or equal to 0",
        value);
  }
}
