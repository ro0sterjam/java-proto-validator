package com.ro0sterware.protovalidator.exceptions;

import com.google.protobuf.Descriptors;
import com.ro0sterware.protovalidator.conditions.ApplyCondition;

public class ApplyConditionNotSupportedException extends RuntimeException {

  public ApplyConditionNotSupportedException(
      ApplyCondition condition, Descriptors.Descriptor messageDescriptor) {
    super(
        String.format(
            "ApplyCondition [%s] is not supported for messageDescriptor: %s",
            condition.getClass().getName(), messageDescriptor));
  }
}
