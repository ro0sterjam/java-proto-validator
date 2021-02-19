package com.ro0sterware.protovalidator.conditions.impl;

import com.google.protobuf.Message;
import com.google.protobuf.StringValue;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import com.ro0sterware.protovalidator.constraints.impl.FieldConstraints;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class StringFieldIsEqualToConditionalTest extends AbstractFieldIsEqualToConditionTest {

  StringFieldIsEqualToConditionalTest() {
    super("hello");
  }

  @Override
  Stream<Arguments> provideApplicableFieldValues() {
    return Stream.of(
        Arguments.arguments("stringField", "hello"),
        Arguments.arguments("stringWrapperField", StringValue.of("hello")));
  }

  @Override
  Stream<Arguments> provideInapplicableFieldValues() {
    return Stream.of(
        Arguments.arguments("stringField", "world"),
        Arguments.arguments("stringWrapperField", StringValue.of("world")));
  }

  @Override
  String getTestField() {
    return "int32Field";
  }

  @Override
  FieldConstraint getTestFieldConstraint() {
    return FieldConstraints.max(3L);
  }

  @Override
  Message.Builder getTestMessageBuilder() {
    return TestMessageOuterClass.TestMessage.newBuilder().setInt32Field(10);
  }

  @Override
  MessageViolation getExpectedMessageViolation(String field, Object value) {
    Map<String, Object> errorCodeParams = new HashMap<>();
    errorCodeParams.put("max", 3L);
    Map<String, Object> conditionCodeParams = new HashMap<>();
    conditionCodeParams.put("field", field);
    conditionCodeParams.put("value", "hello");
    return new MessageViolation(
        getTestField(),
        new MessageViolation.ErrorMessage("field.violations.Max", errorCodeParams),
        new MessageViolation.ConditionMessage(
            "constraint.condition.FieldIsEqualTo", conditionCodeParams),
        "must be less than or equal to 3 when '" + field + "' is equal to " + "hello",
        10);
  }
}
