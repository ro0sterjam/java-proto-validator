package com.ro0sterware.protovalidator.conditions.impl;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import com.ro0sterware.protovalidator.conditions.ApplyCondition;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import com.ro0sterware.protovalidator.constraints.impl.FieldConstraints;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class FieldIsNotTrueConditionTest extends AbstractFieldConditionTest {

  @Override
  ApplyCondition getTestFieldCondition(String field) {
    return ApplyConditions.fieldIsNotTrue(field);
  }

  @Override
  String[] getSupportedConditionalFields() {
    return new String[] {"boolField", "boolWrapperField"};
  }

  @Override
  Stream<Arguments> provideApplicableFieldValues() {
    return Stream.of(
        Arguments.arguments("boolField", false),
        Arguments.arguments("boolWrapperField", null),
        Arguments.arguments("boolWrapperField", BoolValue.of(false)));
  }

  @Override
  Stream<Arguments> provideInapplicableFieldValues() {
    return Stream.of(
        Arguments.arguments("boolField", true),
        Arguments.arguments("boolWrapperField", BoolValue.of(true)));
  }

  @Override
  String getTestField() {
    return "stringField";
  }

  @Override
  FieldConstraint getTestFieldConstraint() {
    return FieldConstraints.length(3, 20);
  }

  @Override
  Message.Builder getTestMessageBuilder() {
    return TestMessageOuterClass.TestMessage.newBuilder().setStringField("ab");
  }

  @Override
  MessageViolation getExpectedMessageViolation(String field, Object value) {
    Map<String, Object> errorCodeParams = new HashMap<>();
    errorCodeParams.put("min", 3);
    errorCodeParams.put("max", 20);
    Map<String, Object> conditionCodeParams = new HashMap<>();
    conditionCodeParams.put("field", field);
    return new MessageViolation(
        "stringField",
        new MessageViolation.ErrorMessage("field.violations.Length", errorCodeParams),
        new MessageViolation.ConditionMessage(
            "constraint.condition.FieldIsNotTrue", conditionCodeParams),
        "length must be between 3 and 20 when " + field + " is not true",
        "ab");
  }
}
