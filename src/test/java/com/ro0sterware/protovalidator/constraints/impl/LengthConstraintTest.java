package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Message;
import com.google.protobuf.StringValue;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import java.util.HashMap;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class LengthConstraintTest extends AbstractFieldConstraintTest {

  @Override
  FieldConstraint getTestFieldConstraint() {
    return FieldConstraints.length(10, 20);
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
        Arguments.of("stringField", "1234567890"),
        Arguments.of("stringField", "12345678901"),
        Arguments.of("stringField", "1234567890123456789"),
        Arguments.of("stringField", "12345678901234567890"),
        Arguments.of("firstOneOfField", null),
        Arguments.of("firstOneOfField", StringValue.of("1234567890")),
        Arguments.of("firstOneOfField", StringValue.of("12345678901")),
        Arguments.of("firstOneOfField", StringValue.of("1234567890123456789")),
        Arguments.of("firstOneOfField", StringValue.of("12345678901234567890")),
        Arguments.of("stringWrapperField", null),
        Arguments.of("stringWrapperField", StringValue.of("1234567890")),
        Arguments.of("stringWrapperField", StringValue.of("12345678901")),
        Arguments.of("stringWrapperField", StringValue.of("1234567890123456789")),
        Arguments.of("stringWrapperField", StringValue.of("12345678901234567890")));
  }

  @Override
  Stream<Arguments> provideInvalidFieldValues() {
    return Stream.of(
        Arguments.of("stringField", "123456789"),
        Arguments.of("stringField", "123456789012345678901"),
        Arguments.of("firstOneOfField", StringValue.of("123456789")),
        Arguments.of("firstOneOfField", StringValue.of("123456789012345678901")),
        Arguments.of("stringWrapperField", StringValue.of("123456789")),
        Arguments.of("stringWrapperField", StringValue.of("123456789012345678901")));
  }

  @Override
  MessageViolation getExpectedMessageViolation(String field, Object value) {
    HashMap<String, Object> errorCodeParams = new HashMap<>();
    errorCodeParams.put("min", 10);
    errorCodeParams.put("max", 20);
    return new MessageViolation(
        field,
        new MessageViolation.ErrorMessage("field.violations.Length", errorCodeParams),
        null,
        "length must be between 10 and 20",
        value);
  }
}
