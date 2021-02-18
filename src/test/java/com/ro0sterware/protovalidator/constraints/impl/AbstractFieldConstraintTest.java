package com.ro0sterware.protovalidator.constraints.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.MessageValidator;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.ProtobufValidator;
import com.ro0sterware.protovalidator.constraints.AbstractRepeatedConstraint;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import com.ro0sterware.protovalidator.exceptions.FieldConstraintNotSupportedException;
import com.ro0sterware.protovalidator.utils.ProtoFieldUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractFieldConstraintTest {

  abstract FieldConstraint getTestFieldConstraint();

  abstract Message.Builder getTestMessageBuilder();

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
  void testValidate_forInvalidMessage_returnsViolation(String field, Object value) {
    ProtobufValidator validator = createValidatorForField(field);
    Message message = createMessageWithFieldValue(field, value);
    assertThat(validator.validate(message))
        .containsOnly(getExpectedMessageViolations(field, value));
  }

  protected void setField(
      Message.Builder messageBuilder, Descriptors.FieldDescriptor fieldDescriptor, Object value) {
    if (value == null) {
      messageBuilder.clearField(fieldDescriptor);
    } else {
      messageBuilder.setField(fieldDescriptor, value);
    }
  }

  private MessageViolation[] getExpectedMessageViolations(String field, Object value) {
    if (value instanceof List
        && !(getTestFieldConstraint() instanceof AbstractRepeatedConstraint)) {
      List<?> elementValues = (List) value;
      return IntStream.range(0, elementValues.size())
          .mapToObj(
              i -> {
                String elementPath = field + "[" + i + "]";
                return getExpectedMessageViolation(elementPath, elementValues.get(i));
              })
          .toArray(MessageViolation[]::new);
    } else {
      return new MessageViolation[] {getExpectedMessageViolation(field, value)};
    }
  }

  private Message createMessageWithFieldValue(String field, Object value) {
    Descriptors.FieldDescriptor fieldDescriptor =
        ProtoFieldUtils.getFieldDescriptor(getTestMessageBuilder().getDescriptorForType(), field);
    Message.Builder messageBuilder = getTestMessageBuilder();
    setField(messageBuilder, fieldDescriptor, value);
    return messageBuilder.build();
  }

  private ProtobufValidator createValidatorForField(String field) {
    return ProtobufValidator.createBuilder()
        .registerValidator(
            MessageValidator.createBuilder(getTestMessageBuilder().getDescriptorForType())
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
    return getTestMessageBuilder().getDescriptorForType().getFields().stream()
        .map(Descriptors.FieldDescriptor::getJsonName)
        .filter(fieldName -> !supportedFieldNames.contains(fieldName));
  }
}
