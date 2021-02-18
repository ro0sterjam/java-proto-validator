package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.MessageViolation;
import com.ro0sterware.protovalidator.TestMessageOuterClass;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class HasKeyConstraintTest extends AbstractFieldConstraintTest {

  @Override
  FieldConstraint getTestFieldConstraint() {
    return FieldConstraints.hasKey("firstName");
  }

  @Override
  Message.Builder getTestMessageBuilder() {
    return TestMessageOuterClass.TestMessage.newBuilder();
  }

  @Override
  String[] getSupportedFields() {
    return new String[] {"mapField"};
  }

  @Override
  Stream<Arguments> provideValidFieldValues() {
    return Stream.of(
        Arguments.of(
            "mapField",
            Collections.singletonMap(
                "firstName", TestMessageOuterClass.TestNestedMessage.newBuilder().build())));
  }

  @Override
  Stream<Arguments> provideInvalidFieldValues() {
    return Stream.of(
        Arguments.of("mapField", Collections.emptyMap()),
        Arguments.of(
            "mapField",
            Collections.singletonMap(
                "lastName", TestMessageOuterClass.TestNestedMessage.newBuilder().build())));
  }

  @Override
  MessageViolation getExpectedMessageViolation(String field, Object value) {
    Map<String, Object> errorCodeParams = Collections.singletonMap("key", "firstName");
    return new MessageViolation(
        field,
        new MessageViolation.ErrorMessage("field.violations.HasKey", errorCodeParams),
        null,
        "must have key 'firstName'",
        value);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void setField(
      Message.Builder messageBuilder, Descriptors.FieldDescriptor fieldDescriptor, Object value) {
    final TestMessageOuterClass.TestMessage.Builder builder =
        (TestMessageOuterClass.TestMessage.Builder) messageBuilder;
    final Map<String, TestMessageOuterClass.TestNestedMessage> values =
        (Map<String, TestMessageOuterClass.TestNestedMessage>) value;
    builder.putAllMapField(values);
  }
}
