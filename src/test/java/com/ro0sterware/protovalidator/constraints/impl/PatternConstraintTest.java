package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Message;
import com.google.protobuf.StringValue;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class PatternConstraintTest extends AbstractFieldConstraintTest {

  @Override
  FieldConstraint getTestFieldConstraint() {
    return FieldConstraints.pattern("ab.*r");
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
      "repeatedStringWrapperField"
    };
  }

  @Override
  Stream<Arguments> provideValidFieldValues() {
    return Stream.of(
        Arguments.of("stringField", "abksjfsdfkr"),
        Arguments.of("firstOneofField", null),
        Arguments.of("firstOneofField", StringValue.of("abksjfsdfkr")),
        Arguments.of("stringWrapperField", null),
        Arguments.of("stringWrapperField", StringValue.of("abksjfsdfkr")),
        Arguments.of("repeatedStringField", Collections.singletonList("abksjfsdfkr")),
        Arguments.of("repeatedStringField", Collections.emptyList()),
        Arguments.of("repeatedStringWrapperField", Collections.emptyList()),
        Arguments.of(
            "repeatedStringWrapperField",
            Collections.singletonList(StringValue.of("abksjfsdfkr"))));
  }

  @Override
  Stream<Arguments> provideInvalidFieldValues() {
    return Stream.of(
        Arguments.of("stringField", "fsdfdsf"),
        Arguments.of("firstOneofField", StringValue.of("werew")),
        Arguments.of("stringWrapperField", StringValue.of("sdfdf")),
        Arguments.of("repeatedStringField", Collections.singletonList("fsdfdsf")),
        Arguments.of(
            "repeatedStringWrapperField", Collections.singletonList(StringValue.of("sdfdf"))));
  }

  @Override
  MessageViolation getExpectedMessageViolation(String field, Object value) {
    Map<String, Object> errorCodeParams = new HashMap<>();
    errorCodeParams.put("regexp", "ab.*r");
    return new MessageViolation(
        field,
        new MessageViolation.ErrorMessage("field.violations.Pattern", errorCodeParams),
        null,
        "must match 'ab.*r'",
        value);
  }
}
