package com.ro0sterware.protovalidator.conditions.impl;

import com.ro0sterware.protovalidator.TestMessageOuterClass;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class EnumFieldIsEqualToConditionalTest extends AbstractFieldIsEqualToConditionTest {

  EnumFieldIsEqualToConditionalTest() {
    super(TestMessageOuterClass.TestEnum.TEST_ENUM_SECOND);
  }

  @Override
  Stream<Arguments> provideApplicableFieldValues() {
    return Stream.of(
        Arguments.arguments("testEnumField", TestMessageOuterClass.TestEnum.TEST_ENUM_SECOND));
  }

  @Override
  Stream<Arguments> provideInapplicableFieldValues() {
    return Stream.of(
        Arguments.arguments("testEnumField", TestMessageOuterClass.TestEnum.TEST_ENUM_FIRST));
  }
}
