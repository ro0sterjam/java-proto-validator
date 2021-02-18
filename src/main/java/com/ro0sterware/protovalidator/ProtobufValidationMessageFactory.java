package com.ro0sterware.protovalidator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Nullable;

/**
 * Factory for retrieving messages from `ProtobufValidationMessages.properties` under the classpath
 */
public class ProtobufValidationMessageFactory {

  private static final Properties DEFAULT_ERROR_MESSAGES = loadDefaultErrorMessages();

  private ProtobufValidationMessageFactory() {}

  /**
   * Get the the message with the given code interpolated with the given params.
   *
   * @param code code of message to return
   * @param params params to interpolate message with
   * @return interpolated message
   */
  @Nullable
  public static String getMessage(String code, Map<String, Object> params) {
    final String defaultErrorMessage = DEFAULT_ERROR_MESSAGES.getProperty(code + ".message");
    if (defaultErrorMessage != null) {
      // Interpolate all violation message params
      return params.entrySet().stream()
          .reduce(
              defaultErrorMessage,
              (oldMessage, param) -> {
                final String name = param.getKey();
                final Object value = param.getValue();
                return oldMessage.replaceAll("\\{" + name + "}", String.valueOf(value));
              },
              (oldMessage, newMessage) -> newMessage);
    }
    return null;
  }

  private static Properties loadDefaultErrorMessages() {
    try (InputStream inputStream =
        ClassLoader.getSystemResourceAsStream("ProtobufValidationMessages.properties")) {
      Properties prop = new Properties();
      prop.load(inputStream);
      return prop;
    } catch (IOException e) {
      throw new RuntimeException("Can't load ProtobufValidationMessages.properties", e);
    }
  }
}
