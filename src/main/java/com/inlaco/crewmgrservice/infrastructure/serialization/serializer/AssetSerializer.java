package com.inlaco.crewmgrservice.infrastructure.serialization.serializer;

import com.inlaco.crewmgrservice.shared.application.port.in.AssetUrlResolver;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

@Component
@RequiredArgsConstructor
@Slf4j
public class AssetSerializer extends ValueSerializer<Asset> {

  private final AssetUrlResolver urlResolver;

  @Override
  public void serialize(Asset asset, JsonGenerator gen, SerializationContext ctxt)
      throws JacksonException {
    if (asset == null) {
      gen.writeNull();
      return;
    }
    if (log.isDebugEnabled()) {
      log.debug("Serializing Asset: publicId={}, type={}", asset.publicId(), asset.type());
    }

    gen.writeStartObject();

    gen.writeStringProperty("type", asset.type());
    gen.writeStringProperty("publicId", asset.publicId());
    gen.writeStringProperty("displayName", asset.displayName());
    gen.writeStringProperty("resourceType", asset.resourceType());
    gen.writeNumberProperty("size", asset.size());
    gen.writeStringProperty("format", asset.format());
    try {
      String url = urlResolver.resolve(asset);

      if (url != null) {
        gen.writeStringProperty("url", url);
      } else {
        log.debug("Asset URL resolved to null for publicId={}", asset.publicId());
      }

    } catch (Exception ex) {
      log.warn("Failed to resolve Asset URL for publicId={}", asset.publicId(), ex);
    }

    gen.writeEndObject();
  }
}
