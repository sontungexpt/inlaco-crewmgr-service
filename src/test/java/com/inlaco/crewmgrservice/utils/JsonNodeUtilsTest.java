package com.inlaco.crewmgrservice.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonNodeUtilsTest {

  private final ObjectMapper objectMapper = RawJsonConvertor.getMapper();

  private JsonNode inputNode = null;

  // Sample JSON for testing
  @BeforeEach
  private void setup() {
    String jsonString =
        """
        {
            "name": "John",
            "age": 30,
            "address": {
                "city": "New York",
                "zip": "10001"
            },
            "contact": {
                "phone": "123456789",
                "email": "john.doe@example.com"
            }
        }
        """;

    try {
      inputNode = objectMapper.readTree(jsonString);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  void validateResult(JsonNode resultNode) {
    assertTrue(resultNode.has("name")); // 'name' should remain
    assertFalse(resultNode.has("age")); // 'age' should be removed
    assertTrue(resultNode.has("address")); // 'address' should still exist
    assertFalse(resultNode.get("address").has("city")); // 'city' should be removed from 'address'
    assertTrue(resultNode.get("address").has("zip")); // 'zip' should remain

    assertTrue(resultNode.has("contact")); // 'contact' should still exist
    assertFalse(resultNode.get("contact").has("phone")); // 'phone' should be removed
    assertTrue(resultNode.get("contact").has("email")); // 'email' should remain
  }

  @Test
  void testRemoveIgnoreFieldsWithArray() throws Exception {
    String[] ignoredFields = {"age", "address.city", "contact.phone"};
    JsonNode resultNode = JsonNodeUtils.removeFields(inputNode, ignoredFields);
    validateResult(resultNode);
  }

  @Test
  void testRemoveIgnoreFieldsWithCollection() throws Exception {
    List<String> ignoredFields = List.of("age", "address.city", "contact.phone");
    JsonNode resultNode = JsonNodeUtils.removeFields(inputNode, ignoredFields);
    validateResult(resultNode);
  }

  @Test
  void testRemoveIgnoreFieldsWithMap() throws Exception {
    Map<String, Object> ignoredFieldsTree =
        Map.of(
            "age", true, // Loại bỏ 'age'
            "address",
                Map.of(
                    "city", true // Loại bỏ 'address.city'
                    ),
            "contact",
                Map.of(
                    "phone", true // Loại bỏ 'contact.phone'
                    ));

    JsonNode resultNode = JsonNodeUtils.removeFields(inputNode, ignoredFieldsTree);

    validateResult(resultNode);
  }
}
