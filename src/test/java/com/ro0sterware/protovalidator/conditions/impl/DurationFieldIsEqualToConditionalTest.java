package com.ro0sterware.protovalidator.conditions.impl;

import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class DurationFieldIsEqualToConditionalTest extends AbstractFieldIsEqualToConditionTest {

  DurationFieldIsEqualToConditionalTest() {
    super(Duration.ofMillis(1613712449225L));
  }

  @Override
  Stream<Arguments> provideApplicableFieldValues() {
    return Stream.of(
        Arguments.arguments(
            "durationField",
            com.google.protobuf.Duration.newBuilder()
                .setSeconds(1613712449)
                .setNanos(225000000)
                .build()));
  }

  @Override
  Stream<Arguments> provideInapplicableFieldValues() {
    return Stream.of(
        Arguments.arguments(
            "durationField",
            com.google.protobuf.Duration.newBuilder().setSeconds(1613712449).setNanos(99).build()));
  }
}
