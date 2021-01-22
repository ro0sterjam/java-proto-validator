package com.ro0sterware.protovalidator.conditions;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.utils.ProtoFieldUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;

/** Abstract {@link ApplyCondition} which applies to the provided field. */
public abstract class AbstractFieldCondition implements ApplyCondition {

  private final String field;

  protected AbstractFieldCondition(String field) {
    this.field = Objects.requireNonNull(field);
  }

  @Override
  public boolean supportsMessage(Descriptors.Descriptor messageDescriptor) {
    final Descriptors.FieldDescriptor fieldDescriptor =
        ProtoFieldUtils.getFieldDescriptor(messageDescriptor, field);
    return supportsField(fieldDescriptor);
  }

  @Override
  public boolean applies(Message message) {
    final Descriptors.FieldDescriptor fieldDescriptor =
        ProtoFieldUtils.getFieldDescriptor(message.getDescriptorForType(), field);
    return applies(message, ProtoFieldUtils.getValue(message, fieldDescriptor));
  }

  /**
   * Return whether or not this condition applies to this message and field value
   *
   * @param message message to check this condition against
   * @param value field value to check this condition against
   * @return whether or not this condition applies
   */
  protected abstract boolean applies(Message message, @Nullable Object value);

  @Override
  public Map<String, Object> getConditionMessageParams() {
    final Map<String, Object> params = new HashMap<>();
    params.put("field", field);
    return Collections.unmodifiableMap(params);
  }

  /**
   * Return whether or not is condition supports the given fieldDescriptor
   *
   * @param fieldDescriptor descriptor of field to apply condition to
   * @return whether or not this condition is supported
   */
  protected abstract boolean supportsField(Descriptors.FieldDescriptor fieldDescriptor);
}
