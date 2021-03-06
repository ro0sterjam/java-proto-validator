package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.Duration;
import com.google.protobuf.FloatValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.Int64Value;
import com.google.protobuf.Message;
import com.google.protobuf.StringValue;
import com.google.protobuf.Timestamp;
import com.google.protobuf.UInt32Value;
import com.google.protobuf.UInt64Value;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class IsNotSetConstraintTest extends AbstractFieldConstraintTest {

  @Override
  FieldConstraint getTestFieldConstraint() {
    return FieldConstraints.IS_NOT_SET;
  }

  @Override
  Message.Builder getTestMessageBuilder() {
    return TestMessageOuterClass.TestMessage.newBuilder();
  }

  @Override
  String[] getSupportedFields() {
    return new String[] {
      "testEnumField",
      "testNestedMessageField",
      "firstOneofField",
      "doubleWrapperField",
      "floatWrapperField",
      "int64WrapperField",
      "uint64WrapperField",
      "int32WrapperField",
      "uint32WrapperField",
      "boolWrapperField",
      "stringWrapperField",
      "bytesWrapperField",
      "timestampField",
      "durationField"
    };
  }

  @Override
  Stream<Arguments> provideValidFieldValues() {
    return Stream.of(
        Arguments.of("testEnumField", null),
        Arguments.of("testNestedMessageField", null),
        Arguments.of("firstOneofField", null),
        Arguments.of("doubleWrapperField", null),
        Arguments.of("floatWrapperField", null),
        Arguments.of("int64WrapperField", null),
        Arguments.of("uint64WrapperField", null),
        Arguments.of("int32WrapperField", null),
        Arguments.of("uint32WrapperField", null),
        Arguments.of("boolWrapperField", null),
        Arguments.of("boolWrapperField", null),
        Arguments.of("stringWrapperField", null),
        Arguments.of("bytesWrapperField", null),
        Arguments.of("timestampField", null),
        Arguments.of("durationField", null));
  }

  @Override
  Stream<Arguments> provideInvalidFieldValues() {
    return Stream.of(
        Arguments.of(
            "testEnumField", TestMessageOuterClass.TestEnum.TEST_ENUM_FIRST.getValueDescriptor()),
        Arguments.of(
            "testEnumField", TestMessageOuterClass.TestEnum.TEST_ENUM_SECOND.getValueDescriptor()),
        Arguments.of(
            "testNestedMessageField", TestMessageOuterClass.TestNestedMessage.getDefaultInstance()),
        Arguments.of("firstOneofField", StringValue.of("someValue")),
        Arguments.of("doubleWrapperField", DoubleValue.of(33.22)),
        Arguments.of("floatWrapperField", FloatValue.of(33.22f)),
        Arguments.of("int64WrapperField", Int64Value.of(33)),
        Arguments.of("uint64WrapperField", UInt64Value.of(33)),
        Arguments.of("int32WrapperField", Int32Value.of(33)),
        Arguments.of("uint32WrapperField", UInt32Value.of(33)),
        Arguments.of("boolWrapperField", BoolValue.of(true)),
        Arguments.of("boolWrapperField", BoolValue.of(false)),
        Arguments.of("stringWrapperField", StringValue.of("someValue")),
        Arguments.of("bytesWrapperField", BytesValue.of(ByteString.EMPTY)),
        Arguments.of("timestampField", Timestamp.getDefaultInstance()),
        Arguments.of("durationField", Duration.getDefaultInstance()));
  }

  @Override
  MessageViolation getExpectedMessageViolation(String field, Object value) {
    return new MessageViolation(
        field,
        new MessageViolation.ErrorMessage("field.violations.IsNotSet", Collections.emptyMap()),
        null,
        "must not be set",
        value);
  }
}
