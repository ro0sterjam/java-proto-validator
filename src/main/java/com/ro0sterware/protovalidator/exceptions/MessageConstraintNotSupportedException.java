package com.ro0sterware.protovalidator.exceptions;

import com.google.protobuf.Descriptors;
import com.ro0sterware.protovalidator.constraints.MessageConstraint;

public class MessageConstraintNotSupportedException extends RuntimeException {

  public MessageConstraintNotSupportedException(
      MessageConstraint messageConstraint, Descriptors.Descriptor messageDescriptor) {
    super(
        String.format(
            "MessageConstraint [%s] is not supported for messageDescriptor: %s",
            messageConstraint.getClass().getName(), messageDescriptor));
  }
}
