package com.ro0sterware.protovalidator.utils;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.exceptions.FieldDoesNotExistException;
import javax.annotation.Nullable;

public class ProtoFieldUtils {

  private ProtoFieldUtils() {}

  public static String toProtoName(String jsonName) {
    final StringBuilder stringBuilder = new StringBuilder();
    for (char c : jsonName.toCharArray()) {
      if (Character.isUpperCase(c)) {
        stringBuilder.append("_");
        stringBuilder.append(Character.toLowerCase(c));
      } else {
        stringBuilder.append(c);
      }
    }
    return stringBuilder.toString();
  }

  @Nullable
  public static Object getValue(Message message, Descriptors.FieldDescriptor fieldDescriptor) {
    if (fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.MESSAGE
        || fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.ENUM) {
      // getField for message and enum just returns the default value even if not set, thus we
      // need to explicitly check if the field is set
      return message.hasField(fieldDescriptor) ? message.getField(fieldDescriptor) : null;
    }
    return message.getField(fieldDescriptor);
  }

  public static Descriptors.FieldDescriptor getFieldDescriptor(
      Descriptors.Descriptor messageDescriptor, String field) {
    final String protoFieldName = toProtoName(field);
    final Descriptors.FieldDescriptor fieldDescriptor =
        messageDescriptor.findFieldByName(protoFieldName);
    if (fieldDescriptor == null) {
      throw new FieldDoesNotExistException(messageDescriptor, field);
    }
    return fieldDescriptor;
  }
}
