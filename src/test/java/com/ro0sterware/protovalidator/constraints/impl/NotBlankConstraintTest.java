package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Message;
import com.google.protobuf.StringValue;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class NotBlankConstraintTest extends AbstractFieldConstraintTest {

  @Override
  FieldConstraint getTestFieldConstraint() {
    return FieldConstraints.NOT_BLANK;
  }

  @Override
  Message.Builder getTestMessageBuilder() {
    return TestMessageOuterClass.TestMessage.newBuilder();
  }

  @Override
  String[] getSupportedFields() {
    return new String[] {
      "stringField",
      "firstOneofField",
      "stringWrapperField",
      "repeatedStringField",
      "repeatedStringWrapperField",
      "conditionalField"
    };
  }

  @Override
  Stream<Arguments> provideValidFieldValues() {
    return Stream.of(
        Arguments.of("stringField", "sdfdsf"),
        Arguments.of("firstOneofField", StringValue.of("sdfsdf")),
        Arguments.of("stringWrapperField", StringValue.of("xcvxcv")),
        Arguments.of("repeatedStringField", Collections.singletonList("sdfdsf")),
        Arguments.of(
            "repeatedStringWrapperField", Collections.singletonList(StringValue.of("xcvxcv"))),
        Arguments.of("repeatedStringField", Collections.emptyList()),
        Arguments.of("repeatedStringWrapperField", Collections.emptyList()));
  }

  @Override
  Stream<Arguments> provideInvalidFieldValues() {
    return Stream.of(
        Arguments.of("stringField", ""),
        Arguments.of("stringField", "   "),
        Arguments.of("firstOneofField", null),
        Arguments.of("firstOneofField", StringValue.of("")),
        Arguments.of("firstOneofField", StringValue.of("   ")),
        Arguments.of("stringWrapperField", null),
        Arguments.of("stringWrapperField", StringValue.of("")),
        Arguments.of("stringWrapperField", StringValue.of("  ")),
        Arguments.of("repeatedStringField", Collections.singletonList("")),
        Arguments.of("repeatedStringField", Collections.singletonList("   ")),
        Arguments.of("repeatedStringWrapperField", Collections.emptyList()),
        Arguments.of("repeatedStringWrapperField", Collections.singletonList(StringValue.of(""))),
        Arguments.of(
            "repeatedStringWrapperField", Collections.singletonList(StringValue.of("  "))));
  }

  @Override
  MessageViolation getExpectedMessageViolation(String field, Object value) {
    return new MessageViolation(
        field,
        new MessageViolation.ErrorMessage("field.violations.NotBlank", Collections.emptyMap()),
        null,
        "must not be blank",
        value);
  }
}
