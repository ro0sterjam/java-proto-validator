package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.AbstractStringConstraint;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

/** Constraint that validates that a String typed field value matches the given regex pattern. */
public class PatternConstraint extends AbstractStringConstraint {

  private final Pattern pattern;

  PatternConstraint(String regexp) {
    this.pattern = Pattern.compile(Objects.requireNonNull(regexp));
  }

  @Override
  protected boolean isValid(
      Message message, Descriptors.FieldDescriptor fieldDescriptor, @Nullable String value) {
    return value == null || pattern.matcher(value).matches();
  }

  @Override
  public String getViolationErrorCode() {
    return "field.violations.Pattern";
  }

  @Override
  public Map<String, Object> getViolationMessageParams() {
    final HashMap<String, Object> params = new HashMap<>();
    params.put("regexp", pattern.pattern());
    return Collections.unmodifiableMap(params);
  }
}
