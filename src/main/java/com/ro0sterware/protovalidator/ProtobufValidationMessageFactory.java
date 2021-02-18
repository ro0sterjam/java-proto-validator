package com.ro0sterware.protovalidator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Nullable;

/**
 * Factory for retrieving messages from `ProtobufValidationMessages.properties` and
 * `DefaultProtobufValidationMessages.properties` under the classpath
 */
public class ProtobufValidationMessageFactory {

  private static final Properties ERROR_MESSAGES =
      loadDefaultErrorMessages("ProtobufValidationMessages.properties");
  private static final Properties DEFAULT_ERROR_MESSAGES =
      loadDefaultErrorMessages("DefaultProtobufValidationMessages.properties");

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
    final String property = code + ".message";
    final String defaultErrorMessage =
        ERROR_MESSAGES.getProperty(property, DEFAULT_ERROR_MESSAGES.getProperty(property));
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

  private static Properties loadDefaultErrorMessages(String filename) {
    try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(filename)) {
      Properties prop = new Properties();
      if (inputStream != null) {
        prop.load(inputStream);
      }
      return prop;
    } catch (IOException e) {
      throw new RuntimeException("Can't load " + filename, e);
    }
  }
}
