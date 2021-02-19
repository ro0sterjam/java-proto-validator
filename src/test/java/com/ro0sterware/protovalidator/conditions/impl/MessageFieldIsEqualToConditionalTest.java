package com.ro0sterware.protovalidator.conditions.impl;

import com.ro0sterware.protovalidator.TestMessageOuterClass;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class MessageFieldIsEqualToConditionalTest extends AbstractFieldIsEqualToConditionTest {

  MessageFieldIsEqualToConditionalTest() {
    super(TestMessageOuterClass.TestNestedMessage.newBuilder().setInt32Field(43).build());
  }

  @Override
  Stream<Arguments> provideApplicableFieldValues() {
    return Stream.of(
        Arguments.arguments(
            "testNestedMessageField",
            TestMessageOuterClass.TestNestedMessage.newBuilder().setInt32Field(43).build()));
  }

  @Override
  Stream<Arguments> provideInapplicableFieldValues() {
    return Stream.of(
        Arguments.arguments(
            "testNestedMessageField",
            TestMessageOuterClass.TestNestedMessage.newBuilder().setInt32Field(33).build()));
  }
}
