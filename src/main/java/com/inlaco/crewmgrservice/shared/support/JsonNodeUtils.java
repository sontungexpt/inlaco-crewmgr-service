package com.inlaco.crewmgrservice.shared.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Collection;
import java.util.Map;

public final class JsonNodeUtils {

  private JsonNodeUtils() {}

  /* =====================================================
   * REMOVE FIELDS BY DOT PATHS
   * ===================================================== */

  /**
   * Removes fields from a JsonNode based on a array of field paths.
   *
   * <p>Field paths are specified in dot-separated format for nested fields. For example,
   * "field1.nestedField" will remove "nestedField" from the "field1" object.
   *
   * @param node The JsonNode from which fields will be removed.
   * @param fieldPaths A array of field paths to be removed. If null or empty, no fields will be
   *     removed.
   * @return The modified JsonNode with specified fields removed.
   */
  public static JsonNode removeFields(JsonNode node, String... fieldPaths) {
    if (!isValidObjectNode(node) || fieldPaths == null || fieldPaths.length == 0) {
      return node;
    }

    for (String path : fieldPaths) {
      removeByPath((ObjectNode) node, path);
    }
    return node;
  }

  public static JsonNode removeFields(JsonNode node, Collection<String> fieldPaths) {
    if (fieldPaths == null || fieldPaths.isEmpty()) {
      return node;
    }
    return removeFields(node, fieldPaths.toArray(String[]::new));
  }

  private static void removeByPath(ObjectNode root, String path) {
    if (path == null || path.isBlank()) return;

    String[] segments = path.split("\\.");
    JsonNode current = root;

    for (int i = 0; i < segments.length - 1; i++) {
      current = current.get(segments[i]);
      if (!(current instanceof ObjectNode)) {
        return;
      }
    }
    ((ObjectNode) current).remove(segments[segments.length - 1]);
  }

  /* =====================================================
   * REMOVE FIELDS BY TREE MAP
   * ===================================================== */
  public static JsonNode removeFields(JsonNode node, Map<String, Object> fieldTree) {
    if (!isValidObjectNode(node) || fieldTree == null || fieldTree.isEmpty()) {
      return node;
    }
    removeRecursive((ObjectNode) node, fieldTree);
    return node;
  }

  private static <V> void removeRecursive(ObjectNode node, Map<String, V> tree) {
    for (Map.Entry<String, V> entry : tree.entrySet()) {
      String field = entry.getKey();
      JsonNode child = node.get(field);
      if (child == null) continue;

      Object rule = entry.getValue();

      if (rule instanceof Map && child instanceof ObjectNode) {
        removeRecursive((ObjectNode) child, (Map<String, V>) rule);
      } else {
        node.remove(field);
      }
    }
  }

  /* =====================================================
   * TYPE COMPATIBILITY
   * ===================================================== */

  public static boolean isTypeCompatible(Class<?> fieldType, JsonNode node) {
    if (node == null || fieldType == null) return false;
    else if (node.isNull()) {
      return !fieldType.isPrimitive();
    } else if (node.isTextual()) {
      return fieldType == String.class || fieldType.isEnum();
    } else if (node.isNumber()) {
      return Number.class.isAssignableFrom(fieldType) || isPrimitiveNumber(fieldType);
    } else if (node.isBoolean()) {
      return fieldType == Boolean.class || fieldType == boolean.class;
    } else if (node.isBinary()) {
      return fieldType == byte[].class;
    } else if (node.isArray()) {
      return fieldType.isArray() || Collection.class.isAssignableFrom(fieldType);
    } else if (node.isObject()) {
      return Map.class.isAssignableFrom(fieldType) || !fieldType.isPrimitive();
    }

    return false;
  }

  private static boolean isPrimitiveNumber(Class<?> type) {
    return type == int.class
        || type == long.class
        || type == double.class
        || type == float.class
        || type == short.class
        || type == byte.class;
  }

  private static boolean isValidObjectNode(JsonNode node) {
    return node instanceof ObjectNode;
  }
}
