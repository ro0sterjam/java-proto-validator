package com.ro0sterware.protovalidator.constraints.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.protobuf.StringValue;
import com.ro0sterware.protovalidator.MessageValidator;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.ProtobufValidator;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class OneOfIsSetConstraintTest {

  @Test
  void testValidation_whenFirstOneofIsSet_returnsNoViolations() {
    ProtobufValidator validator = createValidator();
    TestMessageOuterClass.TestMessage message =
        TestMessageOuterClass.TestMessage.newBuilder()
            .setFirstOneofField(StringValue.of("set"))
            .build();
    assertThat(validator.validate(message)).isEmpty();
  }

  @Test
  void testValidation_whenSecondOneofIsSet_returnsNoViolations() {
    ProtobufValidator validator = createValidator();
    TestMessageOuterClass.TestMessage message =
        TestMessageOuterClass.TestMessage.newBuilder().setSecondOneofField(332).build();
    assertThat(validator.validate(message)).isEmpty();
  }

  @Test
  void testValidation_whenOneofIsNotSet_returnsViolation() {
    ProtobufValidator validator = createValidator();
    TestMessageOuterClass.TestMessage message =
        TestMessageOuterClass.TestMessage.newBuilder().clearOneofField().build();
    Map<String, Object> errorCodeParams = new HashMap<>();
    errorCodeParams.put("fields", Arrays.asList("firstOneofField", "secondOneofField"));
    assertThat(validator.validate(message))
        .containsOnly(
            new MessageViolation(
                "",
                new MessageViolation.ErrorMessage("message.violations.OneOfIsSet", errorCodeParams),
                null,
                "one of [firstOneofField, secondOneofField] must be set",
                message));
  }

  private ProtobufValidator createValidator() {
    return ProtobufValidator.createBuilder()
        .registerValidator(
            MessageValidator.createBuilder(TestMessageOuterClass.TestMessage.getDescriptor())
                .addMessageConstraint(MessageConstraints.oneOfIsSet("oneofField"))
                .build())
        .build();
  }
}
