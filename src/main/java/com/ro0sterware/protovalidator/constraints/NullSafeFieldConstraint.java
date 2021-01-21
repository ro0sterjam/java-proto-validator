package com.ro0sterware.protovalidator.constraints;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.impl.IsSetConstraint;
import javax.annotation.Nullable;

/**
 * Abstract {@link FieldConstraint} which validates to `true` for null values.
 *
 * <p>For fields with this abstract constraint that should validate to `false` for null values,
 * please use in conjunction with {@link IsSetConstraint}
 */
public abstract class NullSafeFieldConstraint implements FieldConstraint {

  @Override
  public boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable Object value) {
    if (value == null) {
      return true;
    }
    return nullSafeIsValid(message, fieldDescriptor, value);
  }

  /**
   * Null safe version of {@link FieldConstraint#isValid(Message, Descriptors.FieldDescriptor,
   * Object)}
   *
   * @param message Message to validate
   * @param fieldDescriptor Descriptor of field to validate
   * @param value null safe value of the field of the given message
   * @return whether or not the field value of the given message is valid
   */
  protected abstract boolean nullSafeIsValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, Object value);
}
