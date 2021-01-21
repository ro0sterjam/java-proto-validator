package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class SizeConstraintTest extends AbstractFieldConstraintTest {

  @Override
  FieldConstraint getTestFieldConstraint() {
    return FieldConstraints.size(2, 3);
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
        Arguments.of("repeatedField", Arrays.asList("some", "strings")),
        Arguments.of("repeatedField", Arrays.asList("some", "more", "strings")));
  }

  @Override
  Stream<Arguments> provideInvalidFieldValues() {
    return Stream.of(
        Arguments.of("repeatedField", Collections.singletonList("lonely")),
        Arguments.of("repeatedField", Arrays.asList("but", "i", "am", "not")));
  }

  @Override
  MessageViolation getExpectedMessageViolation(String field, Object value) {
    HashMap<String, Object> errorCodeParams = new HashMap<>();
    errorCodeParams.put("min", 2);
    errorCodeParams.put("max", 3);
    return new MessageViolation(
        field,
        new MessageViolation.ErrorMessage("field.violations.Size", errorCodeParams),
        null,
        "size must be between 2 and 3",
        value);
  }
}
