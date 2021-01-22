package com.ro0sterware.protovalidator.conditions.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.conditions.ApplyCondition;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/** {@link ApplyCondition} for expressions expressed in Spring Expression Language. */
public class ExpressionCondition implements ApplyCondition {

  private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

  private final Expression expression;

  ExpressionCondition(String expression) {
    this.expression = EXPRESSION_PARSER.parseExpression(Objects.requireNonNull(expression));
  }

  @Override
  public boolean supportsMessage(Descriptors.Descriptor messageDescriptor) {
    return true;
  }

  @Override
  public boolean applies(Message message) {
    try {
      Boolean result = expression.getValue(message, Boolean.class);
      if (result == null) {
        throw new IllegalStateException("Did not expect a null Boolean evaluation result");
      }
      return result;
    } catch (RuntimeException e) {
      throw new ExpressionEvaluationException(
          expression.getExpressionString(), message.getDescriptorForType().getFullName(), e);
    }
  }

  @Override
  public String getConditionCode() {
    return "constraint.condition.Expression";
  }

  @Override
  public Map<String, Object> getConditionMessageParams() {
    final Map<String, Object> params = new HashMap<>();
    params.put("expression", expression.getExpressionString());
    return Collections.unmodifiableMap(params);
  }

  public static class ExpressionEvaluationException extends RuntimeException {

    ExpressionEvaluationException(String expression, String messageType, Throwable cause) {
      super(
          String.format(
              "Failed to evaluate expression: '%s' against message with type: %s",
              expression, messageType),
          cause);
    }
  }
}
