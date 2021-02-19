package com.ro0sterware.protovalidator.conditions.impl;

import com.google.protobuf.BoolValue;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class BooleanFieldIsEqualToConditionalTest extends AbstractFieldIsEqualToConditionTest {

  BooleanFieldIsEqualToConditionalTest() {
    super(true);
  }

  @Override
  Stream<Arguments> provideApplicableFieldValues() {
    return Stream.of(
        Arguments.arguments("boolField", true),
        Arguments.arguments("boolWrapperField", BoolValue.of(true)));
  }

  @Override
  Stream<Arguments> provideInapplicableFieldValues() {
    return Stream.of(
        Arguments.arguments("boolField", false),
        Arguments.arguments("boolWrapperField", BoolValue.of(false)));
  }
}
