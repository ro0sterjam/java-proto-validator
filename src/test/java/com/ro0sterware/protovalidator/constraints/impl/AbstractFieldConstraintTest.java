package com.ro0sterware.protovalidator.constraints.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.ProtocolMessageEnum;
import com.ro0sterware.protovalidator.MessageValidator;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.ProtobufValidator;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import com.ro0sterware.protovalidator.exceptions.FieldConstraintNotSupportedException;
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
abstract class AbstractFieldConstraintTest {

  abstract FieldConstraint getTestFieldConstraint();

  abstract Message getTestMessage();

  abstract String[] getSupportedFields();

  abstract Stream<Arguments> provideValidFieldValues();

  abstract Stream<Arguments> provideInvalidFieldValues();

  abstract MessageViolation getExpectedMessageViolation(String field, Object value);

  @ParameterizedTest
  @MethodSource("provideSupportedFields")
  void testAddFieldConstraint_forSupportedField_doesNotThrowException(String field) {
    createValidatorForField(field);
  }

  @ParameterizedTest
  @MethodSource("provideNonSupportedFields")
  void testAddFieldConstraint_forNonSupportedField_throwsException(String field) {
    assertThatThrownBy(() -> createValidatorForField(field))
        .isInstanceOf(FieldConstraintNotSupportedException.class);
  }

  @ParameterizedTest
  @MethodSource("provideValidFieldValues")
  void testValidate_forValidMessage_returnsNoViolations(String field, Object value) {
    ProtobufValidator validator = createValidatorForField(field);
    Message message = createMessageWithFieldValue(field, value);
    assertThat(validator.validate(message)).isEmpty();
  }

  @ParameterizedTest
  @MethodSource("provideInvalidFieldValues")
  void testValidate_onTrueBoolField_returnsViolation(String field, Object value) {
    ProtobufValidator validator = createValidatorForField(field);
    Message message = createMessageWithFieldValue(field, value);
    assertThat(validator.validate(message)).containsOnly(getExpectedMessageViolation(field, value));
  }

  private Message createMessageWithFieldValue(String field, Object value) {
    String protoFieldName = ProtoFieldUtils.toLowerSnakeCase(field);
    Descriptors.FieldDescriptor fieldDescriptor =
        getTestMessage().getDescriptorForType().findFieldByName(protoFieldName);
    if (value == null) {
      return getTestMessage().newBuilderForType().clearField(fieldDescriptor).build();
    } else if (value instanceof ProtocolMessageEnum) {
      Descriptors.EnumValueDescriptor valueDescriptor =
          ((ProtocolMessageEnum) value).getValueDescriptor();
      return getTestMessage()
          .newBuilderForType()
          .setField(fieldDescriptor, valueDescriptor)
          .build();
    } else {
      return getTestMessage().newBuilderForType().setField(fieldDescriptor, value).build();
    }
  }

  private ProtobufValidator createValidatorForField(String field) {
    return ProtobufValidator.createBuilder()
        .registerValidator(
            MessageValidator.createBuilder(getTestMessage().getDescriptorForType())
                .addFieldConstraint(field, getTestFieldConstraint())
                .build())
        .build();
  }

  private Stream<String> provideSupportedFields() {
    return Arrays.stream(getSupportedFields());
  }

  private Stream<String> provideNonSupportedFields() {
    Set<String> supportedFieldNames =
        Arrays.stream(getSupportedFields()).collect(Collectors.toSet());
    return getTestMessage().getDescriptorForType().getFields().stream()
        .map(Descriptors.FieldDescriptor::getJsonName)
        .filter(fieldName -> !supportedFieldNames.contains(fieldName));
  }
}
