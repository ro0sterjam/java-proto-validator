package com.ro0sterware.protovalidator.conditions;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.ProtobufValidationMessageFactory;
import java.util.Collections;
import java.util.Map;
import javax.annotation.Nullable;

/** Interface for conditions used to determine if a constraint should be applied or not */
public interface ApplyCondition {

  /**
   * Return whether or not this {@link ApplyCondition} can be applied to the given message.
   *
   * @param messageDescriptor descriptor for the message that is being considered
   * @return whether or not this condition supports the given messageDescriptor
   */
  boolean supportsMessage(Descriptors.Descriptor messageDescriptor);

  /**
   * Return true if the condition evaluates to true.
   *
   * @param message message to test this condition against
   * @return whether or not this condition applies to the given message
   */
  boolean applies(Message message);

  /**
   * Return the code for this {@link ApplyCondition}.
   *
   * @return the code for this condition
   */
  String getConditionCode();

  /**
   * Return all parameters that can be used for string interpolation in this condition's message.
   *
   * @return all parameters used for message string interpolation
   */
  default Map<String, Object> getConditionMessageParams() {
    return Collections.emptyMap();
  }

  /**
   * Return the default condition message interpolated with the message params.
   *
   * @return the default condition message
   */
  @Nullable
  default String getDefaultConditionMessage() {
    return ProtobufValidationMessageFactory.getMessage(
        getConditionCode(), getConditionMessageParams());
  }
}
