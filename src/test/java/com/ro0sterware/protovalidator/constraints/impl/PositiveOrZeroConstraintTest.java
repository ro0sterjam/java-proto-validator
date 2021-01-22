package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.DoubleValue;
import com.google.protobuf.FloatValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.Int64Value;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class PositiveOrZeroConstraintTest extends AbstractFieldConstraintTest {

  @Override
  FieldConstraint getTestFieldConstraint() {
    return FieldConstraints.POSITIVE_OR_ZERO;
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
      "int32WrapperField",
      "repeatedDoubleField",
      "repeatedFloatField",
      "repeatedInt32Field",
      "repeatedInt64Field",
      "repeatedSint32Field",
      "repeatedSint64Field",
      "repeatedSfixed32Field",
      "repeatedSfixed64Field",
      "repeatedDoubleWrapperField",
      "repeatedFloatWrapperField",
      "repeatedInt64WrapperField",
      "repeatedInt32WrapperField"
    };
  }

  @Override
  Stream<Arguments> provideValidFieldValues() {
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
        Arguments.of("doubleWrapperField", null),
        Arguments.of("floatWrapperField", FloatValue.of(0.1f)),
        Arguments.of("floatWrapperField", null),
        Arguments.of("int64WrapperField", Int64Value.of(1L)),
        Arguments.of("int64WrapperField", null),
        Arguments.of("int32WrapperField", Int32Value.of(1)),
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
        Arguments.of("int32WrapperField", Int32Value.of(0)),
        Arguments.of("repeatedDoubleField", Collections.singletonList(0.1)),
        Arguments.of("repeatedFloatField", Collections.singletonList(0.1f)),
        Arguments.of("repeatedInt32Field", Collections.singletonList(1)),
        Arguments.of("repeatedInt64Field", Collections.singletonList(1L)),
        Arguments.of("repeatedSint32Field", Collections.singletonList(1)),
        Arguments.of("repeatedSint64Field", Collections.singletonList(1L)),
        Arguments.of("repeatedSfixed32Field", Collections.singletonList(1)),
        Arguments.of("repeatedSfixed64Field", Collections.singletonList(1L)),
        Arguments.of("repeatedDoubleWrapperField", Collections.singletonList(DoubleValue.of(0.1))),
        Arguments.of("repeatedFloatWrapperField", Collections.singletonList(FloatValue.of(0.1f))),
        Arguments.of("repeatedInt64WrapperField", Collections.singletonList(Int64Value.of(1L))),
        Arguments.of("repeatedInt32WrapperField", Collections.singletonList(Int32Value.of(1))),
        Arguments.of("repeatedDoubleField", Collections.singletonList(0.0)),
        Arguments.of("repeatedFloatField", Collections.singletonList(0.0f)),
        Arguments.of("repeatedInt32Field", Collections.singletonList(0)),
        Arguments.of("repeatedInt64Field", Collections.singletonList(0L)),
        Arguments.of("repeatedSint32Field", Collections.singletonList(0)),
        Arguments.of("repeatedSint64Field", Collections.singletonList(0L)),
        Arguments.of("repeatedSfixed32Field", Collections.singletonList(0)),
        Arguments.of("repeatedSfixed64Field", Collections.singletonList(0L)),
        Arguments.of("repeatedDoubleWrapperField", Collections.singletonList(DoubleValue.of(0.0))),
        Arguments.of("repeatedFloatWrapperField", Collections.singletonList(FloatValue.of(0.0f))),
        Arguments.of("repeatedInt64WrapperField", Collections.singletonList(Int64Value.of(0L))),
        Arguments.of("repeatedInt32WrapperField", Collections.singletonList(Int32Value.of(0))),
        Arguments.of("repeatedDoubleField", Collections.emptyList()),
        Arguments.of("repeatedFloatField", Collections.emptyList()),
        Arguments.of("repeatedInt32Field", Collections.emptyList()),
        Arguments.of("repeatedInt64Field", Collections.emptyList()),
        Arguments.of("repeatedSint32Field", Collections.emptyList()),
        Arguments.of("repeatedSint64Field", Collections.emptyList()),
        Arguments.of("repeatedSfixed32Field", Collections.emptyList()),
        Arguments.of("repeatedSfixed64Field", Collections.emptyList()),
        Arguments.of("repeatedDoubleWrapperField", Collections.emptyList()),
        Arguments.of("repeatedFloatWrapperField", Collections.emptyList()),
        Arguments.of("repeatedInt64WrapperField", Collections.emptyList()),
        Arguments.of("repeatedInt32WrapperField", Collections.emptyList()));
  }

  @Override
  Stream<Arguments> provideInvalidFieldValues() {
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
        Arguments.of("floatWrapperField", FloatValue.of(-0.1f)),
        Arguments.of("int64WrapperField", Int64Value.of(-1L)),
        Arguments.of("int32WrapperField", Int32Value.of(-1)),
        Arguments.of("repeatedDoubleField", Collections.singletonList(-0.1)),
        Arguments.of("repeatedFloatField", Collections.singletonList(-0.1f)),
        Arguments.of("repeatedInt32Field", Collections.singletonList(-1)),
        Arguments.of("repeatedInt64Field", Collections.singletonList(-1L)),
        Arguments.of("repeatedSint32Field", Collections.singletonList(-1)),
        Arguments.of("repeatedSint64Field", Collections.singletonList(-1L)),
        Arguments.of("repeatedSfixed32Field", Collections.singletonList(-1)),
        Arguments.of("repeatedSfixed64Field", Collections.singletonList(-1L)),
        Arguments.of("repeatedDoubleWrapperField", Collections.singletonList(DoubleValue.of(-0.1))),
        Arguments.of("repeatedFloatWrapperField", Collections.singletonList(FloatValue.of(-0.1f))),
        Arguments.of("repeatedInt64WrapperField", Collections.singletonList(Int64Value.of(-1L))),
        Arguments.of("repeatedInt32WrapperField", Collections.singletonList(Int32Value.of(-1))));
  }

  @Override
  MessageViolation getExpectedMessageViolation(String field, Object value) {
    return new MessageViolation(
        field,
        new MessageViolation.ErrorMessage(
            "field.violations.PositiveOrZero", Collections.emptyMap()),
        null,
        "must be greater than or equal to 0",
        value);
  }
}
