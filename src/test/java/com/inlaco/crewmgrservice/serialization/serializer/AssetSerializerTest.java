package com.inlaco.crewmgrservice.serialization.serializer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.inlaco.crewmgrservice.infrastructure.serialization.serializer.AssetSerializer;
import com.inlaco.crewmgrservice.shared.application.port.in.AssetUrlResolver;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

class AssetSerializerTest {

  private ObjectMapper mapper;
  private AssetUrlResolver urlResolver;
  private ValueSerializer<Object> serializer;

  @BeforeEach
  void setup() {
    urlResolver = Mockito.mock(AssetUrlResolver.class);
    serializer = Mockito.mock(ValueSerializer.class);
    var module = new SimpleModule();

    module.addSerializer(Asset.class, new AssetSerializer(urlResolver));
    // module.addSerializer(Asset.class, new AssetSerializer(serializer, urlResolver));
    mapper = JsonMapper.builder().addModule(module).build();
  }

  @Test
  void serializer_should_cover_all_fields_of_asset() {
    var fields = Asset.class.getDeclaredFields();
    assertThat(fields)
        .extracting(Field::getName)
        .containsExactlyInAnyOrder(
            "type", "assetId", "publicId", "displayName", "resourceType", "size", "format");
  }

  @Test
  void should_serialize_all_fields_and_add_url_and_remove_assetId() throws Exception {

    // given
    Asset asset =
        Asset.builder()
            .assetId("asset-123")
            .publicId("public-1")
            .displayName("Display Name")
            .type("IMAGE")
            .resourceType("image")
            .format("jpg")
            .size(100L)
            .build();

    when(urlResolver.resolve(asset)).thenReturn("https://cdn/test.jpg");

    // when
    String json = mapper.writeValueAsString(asset);

    // then
    assertThat(json).contains("IMAGE");
    assertThat(json).contains("public-1");
    assertThat(json).contains("Display Name");
    assertThat(json).contains("image");
    assertThat(json).contains("100");
    assertThat(json).contains("jpg");

    assertThat(json).contains("https://cdn/test.jpg");

    // assetId must be removed
    assertThat(json).doesNotContain("asset-123");
  }

  @Test
  void should_not_add_url_when_publicId_is_blank() throws Exception {

    Asset asset =
        Asset.builder()
            .assetId("asset-123")
            .publicId("public-1")
            .displayName("Display Name")
            .type("IMAGE")
            .resourceType("image")
            .format("jpg")
            .size(100L)
            .build();

    String json = mapper.writeValueAsString(asset);

    assertThat(json).doesNotContain("url");
  }
}
