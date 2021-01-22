package com.ro0sterware.protovalidator.constraints.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.protobuf.Message;
import com.google.protobuf.StringValue;
import com.ro0sterware.protovalidator.MessageValidator;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.ProtobufValidator;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class ValidConstraintTest {

  @Test
  void testValidate_withoutValidConstraint_doesNotValidateNested() {
    Message message =
        TestMessageOuterClass.TestMessage.newBuilder()
            .setTestNestedMessageField(
                TestMessageOuterClass.TestNestedMessage.newBuilder()
                    .setInt32Field(-32)
                    .setStringWrapperField(StringValue.of("  "))
                    .build())
            .build();

    ProtobufValidator validator =
        ProtobufValidator.createBuilder()
            .registerValidator(
                MessageValidator.createBuilder(
                        TestMessageOuterClass.TestNestedMessage.getDescriptor())
                    .addFieldConstraint("int32Field", FieldConstraints.POSITIVE)
                    .addFieldConstraint("stringWrapperField", FieldConstraints.NOT_BLANK)
                    .build())
            .build();

    assertThat(validator.validate(message)).isEmpty();
  }

  @Test
  void testValidate_withValidConstraint_validatesNested() {
    Message message =
        TestMessageOuterClass.TestMessage.newBuilder()
            .setTestNestedMessageField(
                TestMessageOuterClass.TestNestedMessage.newBuilder()
                    .setInt32Field(-32)
                    .setStringWrapperField(StringValue.of("  "))
                    .build())
            .build();

    ProtobufValidator validator =
        ProtobufValidator.createBuilder()
            .registerValidator(
                MessageValidator.createBuilder(
                        TestMessageOuterClass.TestNestedMessage.getDescriptor())
                    .addFieldConstraint("int32Field", FieldConstraints.POSITIVE)
                    .addFieldConstraint("stringWrapperField", FieldConstraints.NOT_BLANK)
                    .build())
            .registerValidator(
                MessageValidator.createBuilder(TestMessageOuterClass.TestMessage.getDescriptor())
                    .addFieldConstraint("testNestedMessageField", FieldConstraints.VALID)
                    .build())
            .build();

    assertThat(validator.validate(message))
        .containsOnly(
            new MessageViolation(
                "testNestedMessageField.int32Field",
                new MessageViolation.ErrorMessage(
                    "field.violations.Positive", Collections.emptyMap()),
                null,
                "must be greater than 0",
                -32),
            new MessageViolation(
                "testNestedMessageField.stringWrapperField",
                new MessageViolation.ErrorMessage(
                    "field.violations.NotBlank", Collections.emptyMap()),
                null,
                "must not be blank",
                StringValue.of("  ")));
  }

  @Test
  void testValidate_withoutValidConstraint_doesNotValidateRepeated() {
    Message message =
        TestMessageOuterClass.TestMessage.newBuilder()
            .addRepeatedTestNestedMessageField(
                TestMessageOuterClass.TestNestedMessage.newBuilder()
                    .setInt32Field(-32)
                    .setStringWrapperField(StringValue.of("  "))
                    .build())
            .build();

    ProtobufValidator validator =
        ProtobufValidator.createBuilder()
            .registerValidator(
                MessageValidator.createBuilder(
                        TestMessageOuterClass.TestNestedMessage.getDescriptor())
                    .addFieldConstraint("int32Field", FieldConstraints.POSITIVE)
                    .addFieldConstraint("stringWrapperField", FieldConstraints.NOT_BLANK)
                    .build())
            .build();

    assertThat(validator.validate(message)).isEmpty();
  }

  @Test
  void testValidate_withValidConstraint_validatesRepeatedNested() {
    Message message =
        TestMessageOuterClass.TestMessage.newBuilder()
            .addRepeatedTestNestedMessageField(
                TestMessageOuterClass.TestNestedMessage.newBuilder()
                    .setInt32Field(-32)
                    .setStringWrapperField(StringValue.of("  "))
                    .build())
            .addRepeatedTestNestedMessageField(
                TestMessageOuterClass.TestNestedMessage.newBuilder()
                    .setInt32Field(-18)
                    .setStringWrapperField(StringValue.of(" "))
                    .build())
            .build();

    ProtobufValidator validator =
        ProtobufValidator.createBuilder()
            .registerValidator(
                MessageValidator.createBuilder(
                        TestMessageOuterClass.TestNestedMessage.getDescriptor())
                    .addFieldConstraint("int32Field", FieldConstraints.POSITIVE)
                    .addFieldConstraint("stringWrapperField", FieldConstraints.NOT_BLANK)
                    .build())
            .registerValidator(
                MessageValidator.createBuilder(TestMessageOuterClass.TestMessage.getDescriptor())
                    .addFieldConstraint("repeatedTestNestedMessageField", FieldConstraints.VALID)
                    .build())
            .build();

    assertThat(validator.validate(message))
        .containsOnly(
            new MessageViolation(
                "repeatedTestNestedMessageField[0].int32Field",
                new MessageViolation.ErrorMessage(
                    "field.violations.Positive", Collections.emptyMap()),
                null,
                "must be greater than 0",
                -32),
            new MessageViolation(
                "repeatedTestNestedMessageField[0].stringWrapperField",
                new MessageViolation.ErrorMessage(
                    "field.violations.NotBlank", Collections.emptyMap()),
                null,
                "must not be blank",
                StringValue.of("  ")),
            new MessageViolation(
                "repeatedTestNestedMessageField[1].int32Field",
                new MessageViolation.ErrorMessage(
                    "field.violations.Positive", Collections.emptyMap()),
                null,
                "must be greater than 0",
                -18),
            new MessageViolation(
                "repeatedTestNestedMessageField[1].stringWrapperField",
                new MessageViolation.ErrorMessage(
                    "field.violations.NotBlank", Collections.emptyMap()),
                null,
                "must not be blank",
                StringValue.of(" ")));
  }
}
