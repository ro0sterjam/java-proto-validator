package com.ro0sterware.protovalidator.conditions;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import java.util.Map;
import java.util.Objects;

/** Abstract {@link ApplyCondition} which negates the provided {@link ApplyCondition}. */
public abstract class NotCondition implements ApplyCondition {

  private final ApplyCondition condition;

  protected NotCondition(ApplyCondition condition) {
    this.condition = Objects.requireNonNull(condition);
  }

  @Override
  public boolean supportsMessage(Descriptors.Descriptor messageDescriptor) {
    return condition.supportsMessage(messageDescriptor);
  }

  @Override
  public boolean applies(Message message) {
    return !condition.applies(message);
  }

  @Override
  public Map<String, Object> getConditionMessageParams() {
    return condition.getConditionMessageParams();
  }
}
