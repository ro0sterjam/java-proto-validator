package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.DoubleValue;
import com.google.protobuf.FloatValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.Int64Value;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class MaxConstraintTest extends AbstractFieldConstraintTest {

  @Override
  FieldConstraint getTestFieldConstraint() {
    return FieldConstraints.max(10);
  }

  @Override
  Message.Builder getTestMessageBuilder() {
    return TestMessageOuterClass.TestMessage.newBuilder();
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
        Arguments.of("doubleField", 10.0),
        Arguments.of("floatField", 10.0f),
        Arguments.of("int32Field", 10),
        Arguments.of("int64Field", 10L),
        Arguments.of("sint32Field", 10),
        Arguments.of("sint64Field", 10L),
        Arguments.of("sfixed32Field", 10),
        Arguments.of("sfixed64Field", 10L),
        Arguments.of("secondOneofField", 10),
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
        Arguments.of("doubleField", 10.1),
        Arguments.of("floatField", 10.1f),
        Arguments.of("int32Field", 11),
        Arguments.of("int64Field", 11L),
        Arguments.of("sint32Field", 11),
        Arguments.of("sint64Field", 11L),
        Arguments.of("sfixed32Field", 11),
        Arguments.of("sfixed64Field", 11L),
        Arguments.of("secondOneofField", 11),
        Arguments.of("doubleWrapperField", DoubleValue.of(10.1)),
        Arguments.of("floatWrapperField", FloatValue.of(10.1f)),
        Arguments.of("int64WrapperField", Int64Value.of(11L)),
        Arguments.of("int32WrapperField", Int32Value.of(11)));
  }

  @Override
  MessageViolation getExpectedMessageViolation(String field, Object value) {
    Map<String, Object> errorCodeParams = new HashMap<>();
    errorCodeParams.put("max", 10L);
    return new MessageViolation(
        field,
        new MessageViolation.ErrorMessage("field.violations.Max", errorCodeParams),
        null,
        "must be less than or equal to 10",
        value);
  }
}
