package com.inlaco.crewmgrservice.serialization.deserializer;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.PartyType;
import com.inlaco.crewmgrservice.infrastructure.serialization.deserializer.PatchDeserializer;
import com.inlaco.crewmgrservice.shared.application.model.Patch;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

public class PatchDeserializerTest {

  private JsonMapper mapper;

  @BeforeEach
  void setUp() {
    SimpleModule module = new SimpleModule("PatchModule");
    module.addDeserializer(Patch.class, new PatchDeserializer());

    mapper = JsonMapper.builder().addModule(module).build();
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes({
    @JsonSubTypes.Type(value = LaborPartyDTO.class, name = "LABOR"),
    @JsonSubTypes.Type(value = PartyDTO.class, name = "STATIC")
  })
  static class PartyDTO {
    public String name;
    public PartyType type;

    public PartyDTO() {
      this.type = PartyType.STATIC;
    }

    protected PartyDTO(PartyType type) {
      this.type = type;
    }
  }

  static class LaborPartyDTO extends PartyDTO {
    public String labor;

    public LaborPartyDTO() {
      super(PartyType.LABOR);
    }
  }

  static class TestWrapper {
    public Patch<Map<String, Object>> initiator = Patch.unchanged();
    public Patch<List<String>> tags = Patch.unchanged();
  }

  static class DtoWrapper {
    public Patch<Map<String, Patch<PartyDTO>>> parties = Patch.unchanged();
    public Patch<List<PartyDTO>> partyList = Patch.unchanged();
  }

  // ===============================
  // 1️⃣ Field không xuất hiện
  // ===============================

  @Test
  void shouldRemainUnchangedIfFieldMissing() throws Exception {

    String json = "{}";

    TestWrapper result = mapper.readValue(json, TestWrapper.class);

    assertTrue(result.initiator.isUnchanged());
    assertTrue(result.tags.isUnchanged());
  }

  // ===============================
  // 2️⃣ Field xuất hiện → Updated
  // ===============================

  @Test
  void shouldBeUpdatedWhenFieldPresent() throws Exception {

    String json =
        """
        {
          "initiator": {}
        }
        """;

    TestWrapper result = mapper.readValue(json, TestWrapper.class);

    assertTrue(result.initiator.isUpdated());
    assertNotNull(((Patch.Updated) result.initiator).value());
  }

  // ===============================
  // 3️⃣ Field = null → Updated(null)
  // ===============================

  @Test
  void shouldBeExplicitNullWhenJsonNull() throws Exception {

    String json =
        """
        {
          "initiator": null
        }
        """;

    TestWrapper result = mapper.readValue(json, TestWrapper.class);

    assertTrue(result.initiator.isUpdated());
    assertTrue(result.initiator.isExplicitNull());
  }

  // ===============================
  // 4️⃣ Deep nested Map
  // ===============================

  @Test
  void shouldWrapDeepNestedMapValues() throws Exception {

    String json =
        """
        {
          "initiator": {
            "level1": {
              "level2": {
                "keyNull": null
              }
            }
          }
        }
        """;

    TestWrapper result = mapper.readValue(json, TestWrapper.class);

    assertTrue(result.initiator.isUpdated());
    Map<String, Object> level1Map = ((Patch.Updated<Map<String, Object>>) result.initiator).value();

    Patch<Map<String, Object>> level1 = (Patch<Map<String, Object>>) level1Map.get("level1");

    assertTrue(level1.isUpdated());

    Map<String, Object> level2Map = ((Patch.Updated<Map<String, Object>>) level1).value();

    Patch<Map<String, Object>> level2 = (Patch<Map<String, Object>>) level2Map.get("level2");

    assertTrue(level2.isUpdated());

    Map<String, Object> level3Map = ((Patch.Updated<Map<String, Object>>) level2).value();

    Patch<Object> keyNull = (Patch<Object>) level3Map.get("keyNull");

    assertTrue(keyNull.isUpdated());
    assertTrue(keyNull.isExplicitNull());
  }

  // ===============================
  // 5️⃣ Collection replace toàn bộ (Merge Patch behavior)
  // ===============================

  @Test
  void shouldReplaceCollectionCompletely() throws Exception {

    String json =
        """
        {
          "tags": ["a", "b"]
        }
        """;

    TestWrapper result = mapper.readValue(json, TestWrapper.class);

    assertTrue(result.tags.isUpdated());

    List<String> tags = ((Patch.Updated<List<String>>) result.tags).value();

    assertEquals(2, tags.size());
    assertEquals("a", tags.get(0));
    assertEquals("b", tags.get(1));
  }

  // ===============================
  // 6️⃣ Recursive Map + Polymorphic DTO
  // ===============================

  @Test
  void shouldRecursivelyWrapDtoMapAndPreserveSubtype() throws Exception {

    String json =
        """
        {
          "parties": {
            "p1": {
              "type": "LABOR",
              "name": "John",
              "labor": "Deck"
            }
          }
        }
        """;

    DtoWrapper result = mapper.readValue(json, DtoWrapper.class);

    assertTrue(result.parties.isUpdated());

    Map<String, Patch<PartyDTO>> map =
        ((Patch.Updated<Map<String, Patch<PartyDTO>>>) result.parties).value();

    Patch<PartyDTO> innerPatch = map.get("p1");

    assertTrue(innerPatch.isUpdated());

    PartyDTO dto = ((Patch.Updated<PartyDTO>) innerPatch).value();
    assertTrue(dto instanceof LaborPartyDTO);
    LaborPartyDTO labor = (LaborPartyDTO) dto;

    assertEquals("John", labor.name);
    assertEquals("Deck", labor.labor);
    assertEquals(PartyType.LABOR, labor.type);
  }

  // ===============================
  // 7️⃣ Collection DTO replace + preserve subtype
  // ===============================

  @Test
  void shouldReplaceDtoCollectionAndPreserveSubtype() throws Exception {

    String json =
        """
        {
          "partyList": [
            {
              "type": "LABOR",
              "name": "Alice",
              "labor": "Engine"
            }
          ]
        }
        """;

    DtoWrapper result = mapper.readValue(json, DtoWrapper.class);

    assertTrue(result.partyList.isUpdated());

    List<PartyDTO> list = ((Patch.Updated<List<PartyDTO>>) result.partyList).value();

    assertEquals(1, list.size());

    PartyDTO dto = list.get(0);

    // subtype must be preserved
    assertTrue(dto instanceof LaborPartyDTO);

    LaborPartyDTO labor = (LaborPartyDTO) dto;

    assertEquals("Alice", labor.name);
    assertEquals("Engine", labor.labor);
    assertEquals(PartyType.LABOR, labor.type);
  }
}
