package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.AbstractTimestampConstraint;
import java.time.Instant;
import javax.annotation.Nullable;

/**
 * Constraint that validates that a {@link com.google.protobuf.Timestamp} field value is in the
 * past.
 */
public class PastConstraint extends AbstractTimestampConstraint {

  PastConstraint() {}

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Instant value) {
    return value == null || value.isBefore(Instant.now());
  }

  @Override
  public String getViolationErrorCode() {
    return "field.violations.Past";
  }
}
