package com.ro0sterware.protovalidator.utils;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.ro0sterware.protovalidator.exceptions.FieldDoesNotExistException;
import javax.annotation.Nullable;

public class ProtoFieldUtils {

  private ProtoFieldUtils() {}

  /**
   * Converts the given jsonName (camelCase) of a proto message field to the proto message name
   * (snake_case).
   *
   * @param jsonName the jsonName of the field
   * @return the converted proto name
   */
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

  /**
   * Return the value of the field described by the given field descriptor of the given message or
   * null if not set.
   *
   * <p>Note: `hasField` does not "work" for repeated or non message/non enum fields, thus if not
   * set, the respective default values will be returned instead of null.
   *
   * @param message message in which to retrieve the value from
   * @param fieldDescriptor the descriptor describing the field to retrieve
   * @return value of the field or null if not set
   */
  @Nullable
  public static Object getValue(Message message, Descriptors.FieldDescriptor fieldDescriptor) {
    // Generally, we want to differentiate between fields that are explicitly set.
    // To achieve this we need to call `hasField`, otherwise `getField` will just return
    // a default non-null value even if the value was originally omitted.
    // It would be great to call this for all field types, however we cannot do this for
    // repeated fields (throws exception), nor can we do this for non message or enum fields
    // as `hasField` will return false for "primitive" fields such as `int32` or `string`
    // when their values match the default value (e.g. `0` and `""` respectively). We just
    // have to accept the fact that for these fields we cannot tell the difference between
    // explicitly set and implicitly set to the default values. For these cases, let's just
    // return the value.
    if (fieldDescriptor.isRepeated()
        || (fieldDescriptor.getType() != Descriptors.FieldDescriptor.Type.MESSAGE
            && fieldDescriptor.getType() != Descriptors.FieldDescriptor.Type.ENUM)) {
      return message.getField(fieldDescriptor);
    }
    return message.hasField(fieldDescriptor) ? message.getField(fieldDescriptor) : null;
  }

  /**
   * Return the field descriptor on the given message descriptor with the given field name.
   *
   * @param messageDescriptor descriptor of message
   * @param field name of the field
   * @return field descriptor with given field name on the given message descriptor
   * @throws FieldDoesNotExistException if the field could not be found
   */
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
