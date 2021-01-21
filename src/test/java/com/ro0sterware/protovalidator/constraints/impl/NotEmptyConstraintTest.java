package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class NotEmptyConstraintTest extends AbstractFieldConstraintTest {

  @Override
  FieldConstraint getTestFieldConstraint() {
    return FieldConstraints.NOT_EMPTY;
  }

  @Override
  Message getTestMessage() {
    return TestMessageOuterClass.TestMessage.getDefaultInstance();
  }

  @Override
  String[] getSupportedFields() {
    return new String[] {"repeatedField"};
  }

  @Override
  Stream<Arguments> provideValidFieldValues() {
    return Stream.of(
        Arguments.of("repeatedField", Collections.singletonList("word")),
        Arguments.of("repeatedField", Arrays.asList("some", "strings")));
  }

  @Override
  Stream<Arguments> provideInvalidFieldValues() {
    return Stream.of(Arguments.of("repeatedField", Collections.emptyList()));
  }

  @Override
  MessageViolation getExpectedMessageViolation(String field, Object value) {
    return new MessageViolation(
        field,
        new MessageViolation.ErrorMessage("field.violations.NotEmpty", Collections.emptyMap()),
        null,
        "must not be empty",
        value);
  }
}
