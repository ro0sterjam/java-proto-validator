package com.ro0sterware.protovalidator;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;

/** Container for Message Violations */
public final class MessageViolation {

  private final String path;
  private final ErrorMessage error;
  @Nullable private final ConditionMessage condition;
  private final String defaultMessage;
  @Nullable private final Object value;

  public MessageViolation(
      String path,
      ErrorMessage error,
      @Nullable ConditionMessage condition,
      String defaultMessage,
      @Nullable Object value) {
    this.path = path;
    this.error = error;
    this.condition = condition;
    this.defaultMessage = defaultMessage;
    this.value = value;
  }

  public String getPath() {
    return path;
  }

  public ErrorMessage getError() {
    return error;
  }

  @Nullable
  public ConditionMessage getCondition() {
    return condition;
  }

  public String getDefaultMessage() {
    return defaultMessage;
  }

  @Nullable
  public Object getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MessageViolation)) return false;
    MessageViolation that = (MessageViolation) o;
    return path.equals(that.path)
        && error.equals(that.error)
        && Objects.equals(condition, that.condition)
        && defaultMessage.equals(that.defaultMessage)
        && Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(path, error, condition, defaultMessage, value);
  }

  @Override
  public String toString() {
    return "MessageViolation{"
        + "path='"
        + path
        + '\''
        + ", error="
        + error
        + ", condition="
        + condition
        + ", defaultMessage='"
        + defaultMessage
        + '\''
        + ", value="
        + value
        + '}';
  }

  public static final class ErrorMessage {

    private final String code;
    private final Map<String, Object> params;

    public ErrorMessage(String code, Map<String, Object> params) {
      this.code = code;
      this.params = params;
    }

    public String getCode() {
      return code;
    }

    public Map<String, Object> getParams() {
      return params;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof ErrorMessage)) return false;
      ErrorMessage that = (ErrorMessage) o;
      return code.equals(that.code) && params.equals(that.params);
    }

    @Override
    public int hashCode() {
      return Objects.hash(code, params);
    }

    @Override
    public String toString() {
      return "ErrorMessage{" + "code='" + code + '\'' + ", params=" + params + '}';
    }
  }

  public static final class ConditionMessage {

    private final String code;
    private final Map<String, Object> params;

    public ConditionMessage(String code, Map<String, Object> params) {
      this.code = code;
      this.params = params;
    }

    public String getCode() {
      return code;
    }

    public Map<String, Object> getParams() {
      return params;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof ConditionMessage)) return false;
      ConditionMessage that = (ConditionMessage) o;
      return code.equals(that.code) && params.equals(that.params);
    }

    @Override
    public int hashCode() {
      return Objects.hash(code, params);
    }

    @Override
    public String toString() {
      return "ConditionMessage{" + "code='" + code + '\'' + ", params=" + params + '}';
    }
  }
}
