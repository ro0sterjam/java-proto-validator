package com.ro0sterware.protovalidator.exceptions;

import com.google.protobuf.Descriptors;
import com.ro0sterware.protovalidator.constraints.FieldConstraint;

public class FieldConstraintNotSupportedException extends RuntimeException {

  public FieldConstraintNotSupportedException(
      FieldConstraint fieldConstraint, Descriptors.FieldDescriptor fieldDescriptor) {
    super(
        String.format(
            "FieldConstraint [%s] is not supported for fieldDescriptor: %s",
            fieldConstraint.getClass().getName(), fieldDescriptor));
  }
}
