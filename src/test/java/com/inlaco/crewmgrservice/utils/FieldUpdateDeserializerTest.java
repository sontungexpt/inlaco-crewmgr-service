package com.inlaco.crewmgrservice.utils;

import static org.junit.jupiter.api.Assertions.*;

import com.inlaco.crewmgrservice.infrastructure.serialization.deserializer.FieldUpdateDeserializer;
import com.inlaco.crewmgrservice.shared.application.model.FieldUpdate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference; // CHUẨN JACKSON 3
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

public class FieldUpdateDeserializerTest {

  private JsonMapper mapper;

  @BeforeEach
  void setUp() {
    SimpleModule module = new SimpleModule("FieldUpdateModule");
    module.addDeserializer(FieldUpdate.class, new FieldUpdateDeserializer());

    mapper = JsonMapper.builder().addModule(module).build();
  }

  static class TestWrapper {
    public FieldUpdate<Map<String, Object>> initiator = FieldUpdate.unchanged();
  }

  @Test
  void testDeepNestedMapWithNulls() throws Exception {
    String json =
        """
        {
          "initiator": {
            "level1": {
              "level2": { "keyNull": null }
            }
          }
        }
        """;

    // Test qua Wrapper để Jackson nhận diện được cấu trúc FieldUpdate dễ hơn
    TestWrapper result = mapper.readValue(json, TestWrapper.class);

    assertNotNull(result.initiator);
    Map<String, Object> map1 = result.initiator.get();

    // Bây giờ "level1" sẽ được bọc đúng trong FieldUpdate
    FieldUpdate<Map<String, Object>> level1 = (FieldUpdate<Map<String, Object>>) map1.get("level1");
    assertTrue(level1 instanceof FieldUpdate, "Level 1 phải là FieldUpdate");

    Map<String, Object> map2 = level1.get();
    FieldUpdate<Map<String, Object>> level2 = (FieldUpdate<Map<String, Object>>) map2.get("level2");

    FieldUpdate<Object> keyNull = (FieldUpdate<Object>) level2.get().get("keyNull");
    assertTrue(keyNull.isUnchanged());
  }

  @Test
  void testTopLevelNull() throws Exception {
    String json = "null";
    TypeReference<FieldUpdate<Map<String, Object>>> typeRef = new TypeReference<>() {};

    FieldUpdate<Map<String, Object>> result = mapper.readerFor(typeRef).readValue(json);

    assertNotNull(result);
    assertTrue(result.isUnchanged(), "Nếu JSON gửi null thì FieldUpdate phải là unchanged");
  }
}
