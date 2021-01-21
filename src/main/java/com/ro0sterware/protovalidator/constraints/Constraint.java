package com.ro0sterware.protovalidator.constraints;

import com.ro0sterware.protovalidator.ProtobufValidationMessageFactory;
import java.util.Collections;
import java.util.Map;
import javax.annotation.Nullable;

/** Base interface for {@link FieldConstraint} and {@link MessageConstraint}. */
public interface Constraint {

  /**
   * Return the error code for this {@link FieldConstraint}.
   *
   * @return the error code for this constraint
   */
  String getViolationErrorCode();

  /**
   * Return all parameters that can be used for string interpolation in this constraint's error
   * message.
   *
   * @return all parameters used for error message string interpolation
   */
  default Map<String, Object> getViolationMessageParams() {
    return Collections.emptyMap();
  }

  /**
   * Return this constraint's default error message interpolated with this constraint's message
   * params.
   *
   * @return the default error message
   */
  @Nullable
  default String getDefaultErrorMessage() {
    return ProtobufValidationMessageFactory.getMessage(
        getViolationErrorCode(), getViolationMessageParams());
  }
}
