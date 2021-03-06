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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class MinConstraintTest extends AbstractFieldConstraintTest {

  @Override
  FieldConstraint getTestFieldConstraint() {
    return FieldConstraints.min(10);
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
        Arguments.of("int32WrapperField", null),
        Arguments.of("repeatedDoubleField", Collections.singletonList(10.0)),
        Arguments.of("repeatedFloatField", Collections.singletonList(10.0f)),
        Arguments.of("repeatedInt32Field", Collections.singletonList(10)),
        Arguments.of("repeatedInt64Field", Collections.singletonList(10L)),
        Arguments.of("repeatedSint32Field", Collections.singletonList(10)),
        Arguments.of("repeatedSint64Field", Collections.singletonList(10L)),
        Arguments.of("repeatedSfixed32Field", Collections.singletonList(10)),
        Arguments.of("repeatedSfixed64Field", Collections.singletonList(10L)),
        Arguments.of("repeatedDoubleWrapperField", Collections.singletonList(DoubleValue.of(10.0))),
        Arguments.of("repeatedFloatWrapperField", Collections.singletonList(FloatValue.of(10.0f))),
        Arguments.of("repeatedInt64WrapperField", Collections.singletonList(Int64Value.of(10L))),
        Arguments.of("repeatedInt32WrapperField", Collections.singletonList(Int32Value.of(10))),
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
        Arguments.of("doubleField", 9.9),
        Arguments.of("floatField", 9.9f),
        Arguments.of("int32Field", 9),
        Arguments.of("int64Field", 9L),
        Arguments.of("sint32Field", 9),
        Arguments.of("sint64Field", 9L),
        Arguments.of("sfixed32Field", 9),
        Arguments.of("sfixed64Field", 9L),
        Arguments.of("secondOneofField", 9),
        Arguments.of("doubleWrapperField", DoubleValue.of(9.9)),
        Arguments.of("floatWrapperField", FloatValue.of(9.9f)),
        Arguments.of("int64WrapperField", Int64Value.of(9L)),
        Arguments.of("int32WrapperField", Int32Value.of(9)),
        Arguments.of("repeatedDoubleField", Collections.singletonList(9.9)),
        Arguments.of("repeatedFloatField", Collections.singletonList(9.9f)),
        Arguments.of("repeatedInt32Field", Collections.singletonList(9)),
        Arguments.of("repeatedInt64Field", Collections.singletonList(9L)),
        Arguments.of("repeatedSint32Field", Collections.singletonList(9)),
        Arguments.of("repeatedSint64Field", Collections.singletonList(9L)),
        Arguments.of("repeatedSfixed32Field", Collections.singletonList(9)),
        Arguments.of("repeatedSfixed64Field", Collections.singletonList(9L)),
        Arguments.of("repeatedDoubleWrapperField", Collections.singletonList(DoubleValue.of(9.9))),
        Arguments.of("repeatedFloatWrapperField", Collections.singletonList(FloatValue.of(9.9f))),
        Arguments.of("repeatedInt64WrapperField", Collections.singletonList(Int64Value.of(9L))),
        Arguments.of("repeatedInt32WrapperField", Collections.singletonList(Int32Value.of(9))));
  }

  @Override
  MessageViolation getExpectedMessageViolation(String field, Object value) {
    Map<String, Object> errorCodeParams = new HashMap<>();
    errorCodeParams.put("min", 10L);
    return new MessageViolation(
        field,
        new MessageViolation.ErrorMessage("field.violations.Min", errorCodeParams),
        null,
        "must be greater than or equal to 10",
        value);
  }
}
