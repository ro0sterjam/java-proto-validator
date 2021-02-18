package com.ro0sterware.protovalidator.constraints.impl;

import com.ro0sterware.protovalidator.constraints.MessageConstraint;

public class MessageConstraints {

  private MessageConstraints() {}

  /** Validates that the given OneOf field is set * */
  public static MessageConstraint oneOfIsSet(String field) {
    return new OneOfIsSetConstraint(field);
  }
}
