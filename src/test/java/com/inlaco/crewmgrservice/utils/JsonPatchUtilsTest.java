package com.inlaco.crewmgrservice.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inlaco.crewmgrservice.annotation.JsonPatchIgnore;
import java.util.List;
import org.junit.jupiter.api.Test;

public class JsonPatchUtilsTest {
  ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper

  @Test
  void testRemoveIgnoreFields_shouldRemoveFields() throws Exception {
    String jsonString =
        "{\"name\":\"John\",\"age\":30,\"address\":{\"city\":\"New York\",\"zipcode\":\"10001\"}}";
    JsonNode requestNode = objectMapper.readTree(jsonString);

    // Define the fields to ignore (i.e., remove)
    JsonPatchUtils patchTest = new JsonPatchUtils();
    String[] ignoredFields = {"address.zipcode"};

    JsonNode resultNode = patchTest.removeIgnoreFields(requestNode, ignoredFields);

    // Assert that "zipcode" was removed but "city" remains
    assertFalse(resultNode.has("address.zipcode"));
    assertTrue(resultNode.has("address.city"));
  }

  @Test
  void testGetJsonPatchIgnoreFields_shouldReturnIgnoredFields() {
    class TestClass {
      @JsonPatchIgnore private String ignoredField;
      private String allowedField;
    }

    JsonPatchUtils patchTest = new JsonPatchUtils();
    List<String> ignoredFields = patchTest.getJsonPatchIgnoreFields(TestClass.class);

    // Assert that "ignoredField" is returned, and "allowedField" is not
    assertTrue(ignoredFields.contains("ignoredField"));
    assertFalse(ignoredFields.contains("allowedField"));
  }
}
