package com.ro0sterware.protovalidator.conditions.impl;

import com.google.protobuf.Timestamp;
import java.time.Instant;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class TimestampFieldIsEqualToConditionalTest extends AbstractFieldIsEqualToConditionTest {

  TimestampFieldIsEqualToConditionalTest() {
    super(Instant.ofEpochMilli(1613712449225L));
  }

  @Override
  Stream<Arguments> provideApplicableFieldValues() {
    return Stream.of(
        Arguments.arguments(
            "timestampField",
            Timestamp.newBuilder().setSeconds(1613712449).setNanos(225000000).build()));
  }

  @Override
  Stream<Arguments> provideInapplicableFieldValues() {
    return Stream.of(
        Arguments.arguments(
            "timestampField", Timestamp.newBuilder().setSeconds(1613712449).setNanos(99).build()));
  }
}
