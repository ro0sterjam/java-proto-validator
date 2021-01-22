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
  Message.Builder getTestMessageBuilder() {
    return TestMessageOuterClass.TestMessage.newBuilder();
  }

  @Override
  String[] getSupportedFields() {
    return new String[] {
      "repeatedStringField",
      "repeatedTestNestedMessageField",
      "repeatedDoubleField",
      "repeatedFloatField",
      "repeatedInt32Field",
      "repeatedInt64Field",
      "repeatedUint32Field",
      "repeatedUint64Field",
      "repeatedSint32Field",
      "repeatedSint64Field",
      "repeatedFixed32Field",
      "repeatedFixed64Field",
      "repeatedSfixed32Field",
      "repeatedSfixed64Field",
      "repeatedBoolField",
      "repeatedBytesField",
      "repeatedTestEnumField",
      "repeatedDoubleWrapperField",
      "repeatedFloatWrapperField",
      "repeatedInt64WrapperField",
      "repeatedUint64WrapperField",
      "repeatedInt32WrapperField",
      "repeatedUint32WrapperField",
      "repeatedBoolWrapperField",
      "repeatedStringWrapperField",
      "repeatedBytesWrapperField",
      "repeatedTimestampField",
      "repeatedDurationField"
    };
  }

  @Override
  Stream<Arguments> provideValidFieldValues() {
    return Stream.of(
        Arguments.of("repeatedStringField", Collections.singletonList("word")),
        Arguments.of("repeatedStringField", Arrays.asList("some", "strings")));
  }

  @Override
  Stream<Arguments> provideInvalidFieldValues() {
    return Stream.of(
        Arguments.of("repeatedStringField", Collections.emptyList()),
        Arguments.of("repeatedTestNestedMessageField", Collections.emptyList()));
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
