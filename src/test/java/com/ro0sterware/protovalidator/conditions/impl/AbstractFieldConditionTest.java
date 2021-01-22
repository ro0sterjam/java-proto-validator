package com.ro0sterware.protovalidator.conditions.impl;

import static com.ro0sterware.protovalidator.conditions.impl.ApplyConditions.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.ProtocolMessageEnum;
import com.ro0sterware.protovalidator.MessageValidator;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.ProtobufValidator;
import com.ro0sterware.protovalidator.conditions.ApplyCondition;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import com.ro0sterware.protovalidator.exceptions.ApplyConditionNotSupportedException;
import com.ro0sterware.protovalidator.utils.ProtoFieldUtils;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractFieldConditionTest {

  abstract ApplyCondition getTestFieldCondition(String field);

  abstract String[] getSupportedConditionalFields();

  abstract Stream<Arguments> provideApplicableFieldValues();

  abstract Stream<Arguments> provideInapplicableFieldValues();

  abstract String getTestField();

  abstract FieldConstraint getTestFieldConstraint();

  abstract Message.Builder getTestMessageBuilder();

  abstract MessageViolation getExpectedMessageViolation(String field, Object value);

  @ParameterizedTest
  @MethodSource("provideSupportedConditionalFields")
  void testAddFieldConstraint_forSupportedApplyCondition_doesNotThrowException(String field) {
    createValidatorForConditionalField(field);
  }

  @ParameterizedTest
  @MethodSource("provideNonSupportedConditionalFields")
  void testAddFieldConstraint_forNonSupportedApplyCondition_throwsException(String field) {
    assertThatThrownBy(() -> createValidatorForConditionalField(field))
        .isInstanceOf(ApplyConditionNotSupportedException.class);
  }

  @ParameterizedTest
  @MethodSource("provideApplicableFieldValues")
  void testValidate_forApplicableConditionFieldValues_returnsViolations(
      String field, Object value) {
    ProtobufValidator validator = createValidatorForConditionalField(field);
    Message message = createMessageWithConditionalFieldValue(field, value);
    assertThat(validator.validate(message)).containsOnly(getExpectedMessageViolation(field, value));
  }

  @ParameterizedTest
  @MethodSource("provideInapplicableFieldValues")
  void testValidate_onInapplicableFieldValues_returnsNoViolation(String field, Object value) {
    ProtobufValidator validator = createValidatorForConditionalField(field);
    Message message = createMessageWithConditionalFieldValue(field, value);
    assertThat(validator.validate(message)).isEmpty();
  }

  private Message createMessageWithConditionalFieldValue(String field, Object value) {
    Descriptors.FieldDescriptor fieldDescriptor =
        ProtoFieldUtils.getFieldDescriptor(getTestMessageBuilder().getDescriptorForType(), field);
    if (value == null) {
      return getTestMessageBuilder().clearField(fieldDescriptor).build();
    } else if (value instanceof ProtocolMessageEnum) {
      Descriptors.EnumValueDescriptor valueDescriptor =
          ((ProtocolMessageEnum) value).getValueDescriptor();
      return getTestMessageBuilder().setField(fieldDescriptor, valueDescriptor).build();
    } else {
      return getTestMessageBuilder().setField(fieldDescriptor, value).build();
    }
  }

  private ProtobufValidator createValidatorForConditionalField(String field) {
    final String testField = getTestField();
    final FieldConstraint testFieldConstraint = getTestFieldConstraint();
    final ApplyCondition applyCondition = getTestFieldCondition(field);
    return ProtobufValidator.createBuilder()
        .registerValidator(
            MessageValidator.createBuilder(getTestMessageBuilder().getDescriptorForType())
                .addFieldConstraint(testField, testFieldConstraint, when(applyCondition))
                .build())
        .build();
  }

  private Stream<String> provideSupportedConditionalFields() {
    return Arrays.stream(getSupportedConditionalFields());
  }

  private Stream<String> provideNonSupportedConditionalFields() {
    Set<String> supportedConditionalFieldNames =
        Arrays.stream(getSupportedConditionalFields()).collect(Collectors.toSet());
    return getTestMessageBuilder().getDescriptorForType().getFields().stream()
        .map(Descriptors.FieldDescriptor::getJsonName)
        .filter(fieldName -> !supportedConditionalFieldNames.contains(fieldName));
  }
}
