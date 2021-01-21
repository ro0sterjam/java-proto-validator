package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Message;
import com.google.protobuf.StringValue;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import java.util.HashMap;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class PatternConstraintTest extends AbstractFieldConstraintTest {

  @Override
  FieldConstraint getTestFieldConstraint() {
    return FieldConstraints.pattern("ab.*r");
  }

  @Override
  Message getTestMessage() {
    return TestMessageOuterClass.TestMessage.getDefaultInstance();
  }

  @Override
  String[] getSupportedFields() {
    return new String[] {"stringField", "firstOneOfField", "stringWrapperField"};
  }

  @Override
  Stream<Arguments> provideValidFieldValues() {
    return Stream.of(
        Arguments.of("stringField", "abksjfsdfkr"),
        Arguments.of("firstOneOfField", null),
        Arguments.of("firstOneOfField", StringValue.of("abksjfsdfkr")),
        Arguments.of("stringWrapperField", null),
        Arguments.of("stringWrapperField", StringValue.of("abksjfsdfkr")));
  }

  @Override
  Stream<Arguments> provideInvalidFieldValues() {
    return Stream.of(
        Arguments.of("stringField", "fsdfdsf"),
        Arguments.of("firstOneOfField", StringValue.of("werew")),
        Arguments.of("stringWrapperField", StringValue.of("sdfdf")));
  }

  @Override
  MessageViolation getExpectedMessageViolation(String field, Object value) {
    HashMap<String, Object> errorCodeParams = new HashMap<>();
    errorCodeParams.put("regexp", "ab.*r");
    return new MessageViolation(
        field,
        new MessageViolation.ErrorMessage("field.violations.Pattern", errorCodeParams),
        null,
        "must match 'ab.*r'",
        value);
  }
}
