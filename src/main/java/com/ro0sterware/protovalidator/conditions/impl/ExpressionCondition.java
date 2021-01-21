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
    Boolean result = expression.getValue(message, Boolean.class);
    if (result == null) {
      throw new IllegalStateException(
          String.format(
              "Expression [%s] could not be evaluated to boolean",
              expression.getExpressionString()));
    }
    return result;
  }

  @Override
  public String getConditionCode() {
    return "constraint.condition.Expression";
  }

  @Override
  public Map<String, Object> getConditionMessageParams() {
    final HashMap<String, Object> params = new HashMap<>();
    params.put("expression", expression.getExpressionString());
    return Collections.unmodifiableMap(params);
  }
}
