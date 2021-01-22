package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class AssertTrueConstraintTest extends AbstractFieldConstraintTest {

  @Override
  FieldConstraint getTestFieldConstraint() {
    return FieldConstraints.ASSERT_TRUE;
  }

  @Override
  Message.Builder getTestMessageBuilder() {
    return TestMessageOuterClass.TestMessage.newBuilder();
  }

  @Override
  String[] getSupportedFields() {
    return new String[] {
      "boolField", "boolWrapperField", "repeatedBoolField", "repeatedBoolWrapperField"
    };
  }

  @Override
  Stream<Arguments> provideValidFieldValues() {
    return Stream.of(
        Arguments.of("boolField", true),
        Arguments.of("boolWrapperField", BoolValue.of(true)),
        Arguments.of("boolWrapperField", null),
        Arguments.of("repeatedBoolField", Collections.singletonList(true)),
        Arguments.of("repeatedBoolWrapperField", Collections.singletonList(BoolValue.of(true))),
        Arguments.of("repeatedBoolField", Collections.emptyList()),
        Arguments.of("repeatedBoolWrapperField", Collections.emptyList()));
  }

  @Override
  Stream<Arguments> provideInvalidFieldValues() {
    return Stream.of(
        Arguments.of("boolField", false),
        Arguments.of("boolWrapperField", BoolValue.of(false)),
        Arguments.of("repeatedBoolField", Collections.singletonList(false)),
        Arguments.of("repeatedBoolWrapperField", Collections.singletonList(BoolValue.of(false))));
  }

  @Override
  MessageViolation getExpectedMessageViolation(String field, Object value) {
    return new MessageViolation(
        field,
        new MessageViolation.ErrorMessage("field.violations.AssertTrue", Collections.emptyMap()),
        null,
        "must be true",
        value);
  }
}
