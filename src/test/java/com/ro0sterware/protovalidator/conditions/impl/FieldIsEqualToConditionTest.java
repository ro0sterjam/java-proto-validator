package com.ro0sterware.protovalidator.conditions.impl;

import static com.ro0sterware.protovalidator.conditions.impl.ApplyConditions.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Int32Value;
import com.google.protobuf.Int64Value;
import com.google.protobuf.Message;
import com.google.protobuf.ProtocolMessageEnum;
import com.google.protobuf.StringValue;
import com.google.protobuf.Timestamp;
import com.ro0sterware.protovalidator.MessageValidator;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.ProtobufValidator;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import com.ro0sterware.protovalidator.conditions.ApplyCondition;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import com.ro0sterware.protovalidator.constraints.impl.FieldConstraints;
import com.ro0sterware.protovalidator.exceptions.ApplyConditionNotSupportedException;
import com.ro0sterware.protovalidator.utils.ProtoFieldUtils;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FieldIsEqualToConditionTest {

  ApplyCondition getTestFieldCondition(String field, Object value) {
    return ApplyConditions.fieldIsEqualTo(field, value);
  }

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
      "conditionalField",
    };
  }

  Stream<Arguments> provideApplicableFieldValues() {
    return Stream.of(
        Arguments.arguments("boolField", true, true),
        Arguments.arguments("boolField", false, false),
        Arguments.arguments("boolWrapperField", BoolValue.of(true), true),
        Arguments.arguments("boolWrapperField", BoolValue.of(false), false),
        Arguments.arguments("boolWrapperField", null, null),
        Arguments.arguments("int32Field", 100, 100),
        Arguments.arguments("int32WrapperField", Int32Value.of(100), 100),
        Arguments.arguments("int32WrapperField", null, null),
        Arguments.arguments("int64Field", 100L, 100L),
        Arguments.arguments("int64WrapperField", Int64Value.of(100L), 100L),
        Arguments.arguments("int64WrapperField", null, null),
        Arguments.arguments("stringField", "hello", "hello"),
        Arguments.arguments("stringWrapperField", StringValue.of("hello"), "hello"),
        Arguments.arguments("stringWrapperField", null, null),
        Arguments.arguments(
            "testEnumField",
            TestMessageOuterClass.TestEnum.TEST_ENUM_SECOND,
            TestMessageOuterClass.TestEnum.TEST_ENUM_SECOND),
        Arguments.arguments("testEnumField", null, null),
        Arguments.arguments(
            "timestampField",
            Timestamp.newBuilder().setSeconds(1613712449).setNanos(225000000).build(),
            Instant.ofEpochMilli(1613712449225L)),
        Arguments.arguments("timestampField", null, null),
        Arguments.arguments(
            "durationField",
            com.google.protobuf.Duration.newBuilder()
                .setSeconds(1613712449)
                .setNanos(225000000)
                .build(),
            Duration.ofMillis(1613712449225L)),
        Arguments.arguments("durationField", null, null),
        Arguments.arguments(
            "testNestedMessageField",
            TestMessageOuterClass.TestNestedMessage.newBuilder().setInt32Field(43).build(),
            TestMessageOuterClass.TestNestedMessage.newBuilder().setInt32Field(43).build()),
        Arguments.arguments("testNestedMessageField", null, null));
  }

  Stream<Arguments> provideInapplicableFieldValues() {
    return Stream.of(
        Arguments.arguments("boolField", false, true),
        Arguments.arguments("boolField", true, false),
        Arguments.arguments("boolWrapperField", BoolValue.of(false), true),
        Arguments.arguments("boolWrapperField", BoolValue.of(true), false),
        Arguments.arguments("boolWrapperField", BoolValue.of(true), null),
        Arguments.arguments("boolWrapperField", BoolValue.of(false), null),
        Arguments.arguments("int32Field", 101, 100),
        Arguments.arguments("int32WrapperField", Int32Value.of(101), 100),
        Arguments.arguments("int32WrapperField", Int32Value.of(101), null),
        Arguments.arguments("int64Field", 101L, 100L),
        Arguments.arguments("int64WrapperField", Int64Value.of(101L), 100L),
        Arguments.arguments("int64WrapperField", Int64Value.of(101L), null),
        Arguments.arguments("stringField", "world", "hello"),
        Arguments.arguments("stringWrapperField", StringValue.of("world"), "hello"),
        Arguments.arguments("stringWrapperField", StringValue.of("world"), null),
        Arguments.arguments(
            "testEnumField",
            TestMessageOuterClass.TestEnum.TEST_ENUM_FIRST,
            TestMessageOuterClass.TestEnum.TEST_ENUM_SECOND),
        Arguments.arguments("testEnumField", TestMessageOuterClass.TestEnum.TEST_ENUM_FIRST, null),
        Arguments.arguments(
            "timestampField",
            Timestamp.newBuilder().setSeconds(1613712449).setNanos(99).build(),
            Instant.ofEpochMilli(1613712449225L)),
        Arguments.arguments(
            "timestampField",
            Timestamp.newBuilder().setSeconds(1613712449).setNanos(99).build(),
            null),
        Arguments.arguments(
            "durationField",
            com.google.protobuf.Duration.newBuilder().setSeconds(1613712449).setNanos(99).build(),
            Duration.ofMillis(1613712449225L)),
        Arguments.arguments(
            "durationField",
            com.google.protobuf.Duration.newBuilder().setSeconds(1613712449).setNanos(99).build(),
            null),
        Arguments.arguments(
            "testNestedMessageField",
            TestMessageOuterClass.TestNestedMessage.newBuilder().setInt32Field(33).build(),
            TestMessageOuterClass.TestNestedMessage.newBuilder().setInt32Field(43).build()),
        Arguments.arguments(
            "testNestedMessageField",
            TestMessageOuterClass.TestNestedMessage.newBuilder().setInt32Field(33).build(),
            null));
  }

  String getTestField() {
    return "conditionalField";
  }

  FieldConstraint getTestFieldConstraint() {
    return FieldConstraints.length(3, 20);
  }

  Message.Builder getTestMessageBuilder() {
    return TestMessageOuterClass.TestMessage.newBuilder().setConditionalField("ab");
  }

  @ParameterizedTest
  @MethodSource("provideSupportedConditionalFields")
  void testAddFieldConstraint_forSupportedApplyCondition_doesNotThrowException(String field) {
    createValidatorForConditionalField(field, null);
  }

  @ParameterizedTest
  @MethodSource("provideNonSupportedConditionalFields")
  void testAddFieldConstraint_forNonSupportedApplyCondition_throwsException(String field) {
    assertThatThrownBy(() -> createValidatorForConditionalField(field, null))
        .isInstanceOf(ApplyConditionNotSupportedException.class);
  }

  @ParameterizedTest
  @MethodSource("provideApplicableFieldValues")
  void testValidate_forApplicableConditionFieldValues_returnsViolations(
      String field, Object value, Object conditionalValue) {
    ProtobufValidator validator = createValidatorForConditionalField(field, conditionalValue);
    Message message = createMessageWithConditionalFieldValue(field, value);
    assertThat(validator.validate(message))
        .containsOnly(getExpectedMessageViolation(field, value, conditionalValue));
  }

  @ParameterizedTest
  @MethodSource("provideInapplicableFieldValues")
  void testValidate_onInapplicableFieldValues_returnsNoViolation(
      String field, Object value, Object conditionalValue) {
    ProtobufValidator validator = createValidatorForConditionalField(field, conditionalValue);
    Message message = createMessageWithConditionalFieldValue(field, value);
    assertThat(validator.validate(message)).isEmpty();
  }

  Message createMessageWithConditionalFieldValue(String field, Object value) {
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

  private ProtobufValidator createValidatorForConditionalField(
      String field, Object conditionalValue) {
    final String testField = getTestField();
    final FieldConstraint testFieldConstraint = getTestFieldConstraint();
    final ApplyCondition applyCondition = getTestFieldCondition(field, conditionalValue);
    return ProtobufValidator.createBuilder()
        .registerValidator(
            MessageValidator.createBuilder(getTestMessageBuilder().getDescriptorForType())
                .addFieldConstraint(testField, testFieldConstraint, when(applyCondition))
                .build())
        .build();
  }

  MessageViolation getExpectedMessageViolation(
      String field, Object value, Object conditionalValue) {
    Map<String, Object> errorCodeParams = new HashMap<>();
    errorCodeParams.put("min", 3);
    errorCodeParams.put("max", 20);
    Map<String, Object> conditionCodeParams = new HashMap<>();
    conditionCodeParams.put("field", field);
    conditionCodeParams.put("value", conditionalValue);
    return new MessageViolation(
        getTestField(),
        new MessageViolation.ErrorMessage("field.violations.Length", errorCodeParams),
        new MessageViolation.ConditionMessage(
            "constraint.condition.FieldIsEqualTo", conditionCodeParams),
        "length must be between 3 and 20 when '" + field + "' is equal to " + conditionalValue,
        "ab");
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
