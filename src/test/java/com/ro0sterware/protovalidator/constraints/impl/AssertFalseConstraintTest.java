package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class AssertFalseConstraintTest extends AbstractFieldConstraintTest {

  @Override
  FieldConstraint getTestFieldConstraint() {
    return FieldConstraints.ASSERT_FALSE;
  }

  @Override
  Message getTestMessage() {
    return TestMessageOuterClass.TestMessage.getDefaultInstance();
  }

  @Override
  String[] getSupportedFields() {
    return new String[] {"boolField", "boolWrapperField"};
  }

  @Override
  Stream<Arguments> provideValidFieldValues() {
    return Stream.of(
        Arguments.of("boolField", false),
        Arguments.of("boolWrapperField", BoolValue.of(false)),
        Arguments.of("boolWrapperField", null));
  }

  @Override
  Stream<Arguments> provideInvalidFieldValues() {
    return Stream.of(
        Arguments.of("boolField", true), Arguments.of("boolWrapperField", BoolValue.of(true)));
  }

  @Override
  MessageViolation getExpectedMessageViolation(String field, Object value) {
    return new MessageViolation(
        field,
        new MessageViolation.ErrorMessage("field.violations.AssertFalse", Collections.emptyMap()),
        null,
        "must be false",
        value);
  }
}
