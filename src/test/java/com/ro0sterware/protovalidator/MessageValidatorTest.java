package com.ro0sterware.protovalidator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ro0sterware.protovalidator.constraints.impl.FieldConstraints;
import com.ro0sterware.protovalidator.constraints.impl.MessageConstraints;
import com.ro0sterware.protovalidator.exceptions.FieldDoesNotExistException;
import com.ro0sterware.protovalidator.exceptions.MessageConstraintNotSupportedException;
import org.junit.jupiter.api.Test;

class MessageValidatorTest {

  @Test
  void testAddFieldConstraint_forNonExistentField_throwsException() {
    assertThatThrownBy(
            () ->
                createMessageValidatorBuilder()
                    .addFieldConstraint("nonExistent", FieldConstraints.ASSERT_FALSE))
        .isInstanceOf(FieldDoesNotExistException.class);
  }

  @Test
  void testAddMessageConstraint_forNonExistentOneOfParam_throwsException() {
    assertThatThrownBy(
            () ->
                createMessageValidatorBuilder()
                    .addMessageConstraint(MessageConstraints.oneOfIsSet("nonExistent")))
        .isInstanceOf(MessageConstraintNotSupportedException.class);
  }

  private MessageValidator.Builder createMessageValidatorBuilder() {
    return MessageValidator.createBuilder(TestMessageOuterClass.TestMessage.getDescriptor());
  }
}
