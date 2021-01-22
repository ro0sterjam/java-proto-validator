package com.ro0sterware.protovalidator.conditions.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.protobuf.Int32Value;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.MessageValidator;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.ProtobufValidator;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import com.ro0sterware.protovalidator.conditions.ApplyCondition;
import com.ro0sterware.protovalidator.constraints.impl.FieldConstraints;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ExpressionConditionTest {

  @Test
  void testValidate_whenExpressionApplies_returnsViolation() {
    ApplyCondition applyCondition = ApplyConditions.expression("int32WrapperField.value > 99");

    Message message = createTestMessageBuilder().setInt32WrapperField(Int32Value.of(100)).build();

    ProtobufValidator validator = createValidatorWithApplyCondition(applyCondition);

    Map<String, Object> conditionCodeParams = new HashMap<>();
    conditionCodeParams.put("expression", "int32WrapperField.value > 99");
    assertThat(validator.validate(message))
        .containsOnly(
            new MessageViolation(
                "int32Field",
                new MessageViolation.ErrorMessage(
                    "field.violations.Positive", Collections.emptyMap()),
                new MessageViolation.ConditionMessage(
                    "constraint.condition.Expression", conditionCodeParams),
                "must be greater than 0 when int32WrapperField.value > 99",
                -32));
  }

  @Test
  void testValidate_whenExpressionDoesNotApplies_returnsNoViolations() {
    ApplyCondition applyCondition = ApplyConditions.expression("int32WrapperField.value > 99");

    Message message = createTestMessageBuilder().setInt32WrapperField(Int32Value.of(99)).build();

    ProtobufValidator validator = createValidatorWithApplyCondition(applyCondition);

    assertThat(validator.validate(message)).isEmpty();
  }

  @Test
  void testValidate_whenExpressionCannotEvaluate_throwsException() {
    ApplyCondition applyCondition = ApplyConditions.expression("nonsense > 99");

    Message message = createTestMessageBuilder().build();

    ProtobufValidator validator = createValidatorWithApplyCondition(applyCondition);

    assertThatThrownBy(() -> validator.validate(message))
        .isInstanceOf(ExpressionCondition.ExpressionEvaluationException.class);
  }

  @Test
  void testValidate_whenExpressionDoesNotEvaluateToBoolean_throwsException() {
    ApplyCondition applyCondition = ApplyConditions.expression("int32WrapperField");

    Message message = createTestMessageBuilder().build();

    ProtobufValidator validator = createValidatorWithApplyCondition(applyCondition);

    assertThatThrownBy(() -> validator.validate(message))
        .isInstanceOf(ExpressionCondition.ExpressionEvaluationException.class);
  }

  private ProtobufValidator createValidatorWithApplyCondition(ApplyCondition applyCondition) {
    return ProtobufValidator.createBuilder()
        .registerValidator(
            MessageValidator.createBuilder(TestMessageOuterClass.TestMessage.getDescriptor())
                .addFieldConstraint("int32Field", FieldConstraints.POSITIVE, applyCondition)
                .build())
        .build();
  }

  private TestMessageOuterClass.TestMessage.Builder createTestMessageBuilder() {
    return TestMessageOuterClass.TestMessage.newBuilder()
        .setInt32Field(-32)
        .setInt32WrapperField(Int32Value.of(99));
  }
}
