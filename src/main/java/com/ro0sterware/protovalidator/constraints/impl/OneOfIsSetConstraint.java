package com.ro0sterware.protovalidator.constraints.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.constraints.MessageConstraint;
import com.ro0sterware.protovalidator.utils.ProtoFieldUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/** Constraint that validates that a oneOf field on a message is set. */
public class OneOfIsSetConstraint implements MessageConstraint {

  private final String field;
  private final String protoFieldName;
  private Descriptors.OneofDescriptor oneOfDescriptor;

  OneOfIsSetConstraint(String field) {
    this.field = Objects.requireNonNull(field);
    this.protoFieldName = ProtoFieldUtils.toProtoName(field);
  }

  @Override
  public boolean supportsMessage(Descriptors.Descriptor messageDescriptor) {
    return messageDescriptor.getOneofs().stream()
        .anyMatch(descriptor -> descriptor.getName().equals(protoFieldName));
  }

  @Override
  public boolean isValid(Message message) {
    return message.hasOneof(getOneOfDescriptor(message));
  }

  @Override
  public String getViolationErrorCode() {
    return "message.violations.OneOfIsSet";
  }

  @Override
  public Map<String, Object> getViolationMessageParams() {
    // `getViolationMessageParams` should only be called after `isValid`, thus oneOfDescriptor
    // should not be null at this point
    final List<String> fields =
        oneOfDescriptor.getFields().stream()
            .map(Descriptors.FieldDescriptor::getJsonName)
            .collect(Collectors.toList());
    final Map<String, Object> params = new HashMap<>();
    params.put("fields", fields);
    return Collections.unmodifiableMap(params);
  }

  private Descriptors.OneofDescriptor getOneOfDescriptor(Message message) {
    if (oneOfDescriptor == null) {
      oneOfDescriptor =
          message.getDescriptorForType().getOneofs().stream()
              .filter(descriptor -> descriptor.getName().equals(protoFieldName))
              .findFirst()
              .orElseThrow(() -> new IllegalStateException("Expected oneOf field: " + field));
    }
    return oneOfDescriptor;
  }
}
