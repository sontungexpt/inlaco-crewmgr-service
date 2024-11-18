package com.inlaco.crewmgrservice.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inlaco.crewmgrservice.annotation.JsonPatchIgnore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
public class JsonPatchUtilsTest {

  private static class ParentClass {
    private String includedField;
    @JsonPatchIgnore private String ignoredField;

    private NestedClass nestedObject;
  }

  private static class NestedClass {
    private String includedNestedField;
    @JsonPatchIgnore private String ignoredNestedField;
    private Nested2Class nested2Object;
  }

  private static class Nested2Class {
    private String includedNested2Field;
    @JsonPatchIgnore private String ignoredNested2Field;
  }

  @Mock private MongoTemplate mongoTemplate;
  @InjectMocks private JsonMergePatchUtils jsonMergePatchUtils;

  private final ObjectMapper objectMapper = RawJsonConvertor.getMapper();
  private JsonNode inputNode = null;

  // Sample JSON for testing
  @BeforeEach
  private void setup() {
    String jsonString =
        """
            {
                "includedField": "John",
                "ignoredField": "10001",
                "nestedObject": {
                    "includedNestedField": "New York",
                    "ignoredNestedField": "10001",
                    "nested2Object": {
                      "includedNested2Field": "Doe",
                      "ignoredNested2Field": "John"
                    }
                }
            }
        """;

    try {

      inputNode = objectMapper.readTree(jsonString);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void testGetJsonPatchIgnoreAsPath() {
    JsonNode ignoredFields =
        jsonMergePatchUtils.removeJsonPatchIgnoreFields(inputNode, ParentClass.class);
    assertTrue(ignoredFields.has("includedField"));
    assertTrue(ignoredFields.has("nestedObject"));
    assertFalse(ignoredFields.has("ignoredField"));
    assertTrue(ignoredFields.get("nestedObject").has("includedNestedField"));
    assertFalse(ignoredFields.get("nestedObject").has("ignoredNestedField"));
    assertTrue(ignoredFields.get("nestedObject").has("nested2Object"));
    assertTrue(ignoredFields.get("nestedObject").get("nested2Object").has("includedNested2Field"));
    assertFalse(ignoredFields.get("nestedObject").get("nested2Object").has("ignoredNested2Field"));
  }
}
