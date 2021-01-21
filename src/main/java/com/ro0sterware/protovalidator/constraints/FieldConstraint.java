package com.ro0sterware.protovalidator.constraints;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import javax.annotation.Nullable;

/** Interface for constraints mapped to {@link Message} fields. */
public interface FieldConstraint extends Constraint {

  /**
   * Return whether or not this {@link FieldConstraint} can be applied to the given field.
   *
   * @param fieldDescriptor descriptor for the field that is being constrained
   * @return whether or not this constraint supports the given fieldDescriptor
   */
  boolean supportsField(Descriptors.FieldDescriptor fieldDescriptor);

  /**
   * Return whether or not the field value of this message is valid based on this constraint.
   *
   * @param message Message to validate
   * @param fieldDescriptor Descriptor of field to validate
   * @param value Value of the field of the given message
   * @return whether or not the field value of the given message is valid
   */
  boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Object value);
}
