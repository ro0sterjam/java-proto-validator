package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Message;
import com.google.protobuf.Timestamp;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class PastConstraintTest extends AbstractFieldConstraintTest {

  @Override
  FieldConstraint getTestFieldConstraint() {
    return FieldConstraints.PAST;
  }

  @Override
  Message.Builder getTestMessageBuilder() {
    return TestMessageOuterClass.TestMessage.newBuilder();
  }

  @Override
  String[] getSupportedFields() {
    return new String[] {"timestampField"};
  }

  @Override
  Stream<Arguments> provideValidFieldValues() {
    return Stream.of(
        Arguments.of("timestampField", null),
        Arguments.of("timestampField", fromInstant(Instant.now().minus(1, ChronoUnit.MINUTES))));
  }

  @Override
  Stream<Arguments> provideInvalidFieldValues() {
    return Stream.of(
        Arguments.of("timestampField", fromInstant(Instant.now().plus(1, ChronoUnit.MINUTES))));
  }

  @Override
  MessageViolation getExpectedMessageViolation(String field, Object value) {
    return new MessageViolation(
        field,
        new MessageViolation.ErrorMessage("field.violations.Past", Collections.emptyMap()),
        null,
        "must be a past date",
        value);
  }

  private static Timestamp fromInstant(Instant instant) {
    return Timestamp.newBuilder()
        .setSeconds(instant.getEpochSecond())
        .setNanos((int) (instant.toEpochMilli() % 1000))
        .build();
  }
}
