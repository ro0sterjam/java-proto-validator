package com.ro0sterware.protovalidator.conditions.impl;

import com.google.protobuf.Int32Value;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class IntegerFieldIsEqualToConditionalTest extends AbstractFieldIsEqualToConditionTest {

  IntegerFieldIsEqualToConditionalTest() {
    super(100);
  }

  @Override
  Stream<Arguments> provideApplicableFieldValues() {
    return Stream.of(
        Arguments.arguments("int32Field", 100),
        Arguments.arguments("int32WrapperField", Int32Value.of(100)));
  }

  @Override
  Stream<Arguments> provideInapplicableFieldValues() {
    return Stream.of(
        Arguments.arguments("int32Field", 101),
        Arguments.arguments("int32WrapperField", Int32Value.of(101)));
  }
}
