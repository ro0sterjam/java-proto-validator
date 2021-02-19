package com.ro0sterware.protovalidator.conditions.impl;

import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import com.ro0sterware.protovalidator.conditions.ApplyCondition;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import com.ro0sterware.protovalidator.constraints.impl.FieldConstraints;
import java.util.HashMap;
import java.util.Map;

abstract class AbstractFieldIsEqualToConditionTest extends AbstractFieldConditionTest {

  private final Object value;

  protected AbstractFieldIsEqualToConditionTest(Object value) {
    this.value = value;
  }

  @Override
  ApplyCondition getTestFieldCondition(String field) {
    return ApplyConditions.fieldIsEqualTo(field, value);
  }

  @Override
  String[] getSupportedConditionalFields() {
    return new String[] {
      "testEnumField",
      "testNestedMessageField",
      "timestampField",
      "durationField",
      "doubleField",
      "floatField",
      "boolField",
      "stringField",
      "int32Field",
      "uint32Field",
      "fixed32Field",
      "int64Field",
      "uint64Field",
      "fixed64Field",
      "sint32Field",
      "sint64Field",
      "sfixed32Field",
      "sfixed64Field",
      "bytesField",
      "doubleWrapperField",
      "floatWrapperField",
      "int64WrapperField",
      "uint64WrapperField",
      "int32WrapperField",
      "uint32WrapperField",
      "boolWrapperField",
      "stringWrapperField",
      "bytesWrapperField",
      "firstOneofField",
      "secondOneofField",
    };
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
    conditionCodeParams.put("value", this.value);
    return new MessageViolation(
        getTestField(),
        new MessageViolation.ErrorMessage("field.violations.Length", errorCodeParams),
        new MessageViolation.ConditionMessage(
            "constraint.condition.FieldIsEqualTo", conditionCodeParams),
        "length must be between 3 and 20 when '" + field + "' is equal to " + this.value,
        "ab");
  }
}
