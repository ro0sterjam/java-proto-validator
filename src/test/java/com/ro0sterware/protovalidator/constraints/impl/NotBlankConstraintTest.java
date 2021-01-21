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
  Message getTestMessage() {
    return TestMessageOuterClass.TestMessage.getDefaultInstance();
  }

  @Override
  String[] getSupportedFields() {
    return new String[] {"stringField", "firstOneofField", "stringWrapperField"};
  }

  @Override
  Stream<Arguments> provideValidFieldValues() {
    return Stream.of(
        Arguments.of("stringField", "sdfdsf"),
        Arguments.of("firstOneofField", StringValue.of("sdfsdf")),
        Arguments.of("stringWrapperField", StringValue.of("xcvxcv")));
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
        Arguments.of("stringWrapperField", StringValue.of("  ")));
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
