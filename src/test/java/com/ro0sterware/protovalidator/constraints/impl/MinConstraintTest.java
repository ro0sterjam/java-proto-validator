package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.*;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import java.util.HashMap;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class MinConstraintTest extends AbstractFieldConstraintTest {

  @Override
  FieldConstraint getTestFieldConstraint() {
    return FieldConstraints.min(10);
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
      "secondOneOfField",
      "doubleWrapperField",
      "floatWrapperField",
      "int64WrapperField",
      "int32WrapperField"
    };
  }

  @Override
  Stream<Arguments> provideValidFieldValues() {
    return Stream.of(
        Arguments.of("doubleField", 10.0),
        Arguments.of("floatField", 10.0f),
        Arguments.of("int32Field", 10),
        Arguments.of("int64Field", 10L),
        Arguments.of("sint32Field", 10),
        Arguments.of("sint64Field", 10L),
        Arguments.of("sfixed32Field", 10),
        Arguments.of("sfixed64Field", 10L),
        Arguments.of("secondOneOfField", 10),
        Arguments.of("doubleWrapperField", DoubleValue.of(10.0)),
        Arguments.of("doubleWrapperField", null),
        Arguments.of("floatWrapperField", FloatValue.of(10.0f)),
        Arguments.of("floatWrapperField", null),
        Arguments.of("int64WrapperField", Int64Value.of(10L)),
        Arguments.of("int64WrapperField", null),
        Arguments.of("int32WrapperField", Int32Value.of(10)),
        Arguments.of("int32WrapperField", null));
  }

  @Override
  Stream<Arguments> provideInvalidFieldValues() {
    return Stream.of(
        Arguments.of("doubleField", 9.9),
        Arguments.of("floatField", 9.9f),
        Arguments.of("int32Field", 9),
        Arguments.of("int64Field", 9L),
        Arguments.of("sint32Field", 9),
        Arguments.of("sint64Field", 9L),
        Arguments.of("sfixed32Field", 9),
        Arguments.of("sfixed64Field", 9L),
        Arguments.of("secondOneOfField", 9),
        Arguments.of("doubleWrapperField", DoubleValue.of(9.9)),
        Arguments.of("floatWrapperField", FloatValue.of(9.9f)),
        Arguments.of("int64WrapperField", Int64Value.of(9L)),
        Arguments.of("int32WrapperField", Int32Value.of(9)));
  }

  @Override
  MessageViolation getExpectedMessageViolation(String field, Object value) {
    HashMap<String, Object> errorCodeParams = new HashMap<>();
    errorCodeParams.put("min", 10L);
    return new MessageViolation(
        field,
        new MessageViolation.ErrorMessage("field.violations.Min", errorCodeParams),
        null,
        "must be greater than or equal to 10",
        value);
  }
}
