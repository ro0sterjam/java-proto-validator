package com.ro0sterware.protovalidator.utils;

public class ProtoFieldUtils {

  private ProtoFieldUtils() {}

  public static String toLowerCamelCase(String lowerSnakeCase) {
    // TODO verify input is lower snake case
    final StringBuilder stringBuilder = new StringBuilder();
    boolean capitalizeNext = false;
    for (char c : lowerSnakeCase.toCharArray()) {
      if (c == '_') {
        capitalizeNext = true;
      } else if (capitalizeNext && Character.isLowerCase(c)) {
        stringBuilder.append(Character.toUpperCase(c));
        capitalizeNext = false;
      } else {
        stringBuilder.append(c);
        capitalizeNext = false;
      }
    }
    return stringBuilder.toString();
  }

  public static String toLowerSnakeCase(String lowerCamelCase) {
    // TODO verify input is lower camel case
    final StringBuilder stringBuilder = new StringBuilder();
    for (char c : lowerCamelCase.toCharArray()) {
      if (Character.isUpperCase(c)) {
        stringBuilder.append("_");
        stringBuilder.append(Character.toLowerCase(c));
      } else {
        stringBuilder.append(c);
      }
    }
    return stringBuilder.toString();
  }
}
