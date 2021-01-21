package com.ro0sterware.protovalidator.constraints;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;

/** Interface for constraints mapped to a {@link Message} as a whole. */
public interface MessageConstraint extends Constraint {

  /**
   * Return whether or not this constraint supports the given message type.
   *
   * @param messageDescriptor descriptor for the message that is being constrained
   * @return whether or not this constraint supports the given message type
   */
  boolean supportsMessage(Descriptors.Descriptor messageDescriptor);

  /**
   * Return whether or not this message is valid based on this constraint.
   *
   * @param message message to validate
   * @return whether or not this message is valid
   */
  boolean isValid(Message message);
}
