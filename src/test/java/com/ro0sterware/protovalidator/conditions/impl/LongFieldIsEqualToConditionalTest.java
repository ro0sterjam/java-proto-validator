package com.ro0sterware.protovalidator.conditions.impl;

import com.google.protobuf.Int64Value;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class LongFieldIsEqualToConditionalTest extends AbstractFieldIsEqualToConditionTest {

  LongFieldIsEqualToConditionalTest() {
    super(100L);
  }

  @Override
  Stream<Arguments> provideApplicableFieldValues() {
    return Stream.of(
        Arguments.arguments("int64Field", 100L),
        Arguments.arguments("int64WrapperField", Int64Value.of(100L)));
  }

  @Override
  Stream<Arguments> provideInapplicableFieldValues() {
    return Stream.of(
        Arguments.arguments("int64Field", 101L),
        Arguments.arguments("int64WrapperField", Int64Value.of(101L)));
  }
}
