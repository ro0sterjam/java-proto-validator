package com.ro0sterware.protovalidator.exceptions;

import com.google.protobuf.Descriptors;

public class FieldDoesNotExistException extends RuntimeException {

  public FieldDoesNotExistException(Descriptors.Descriptor descriptor, String field) {
    super(String.format("Message %s does not contain field %s", descriptor.getName(), field));
  }
}
