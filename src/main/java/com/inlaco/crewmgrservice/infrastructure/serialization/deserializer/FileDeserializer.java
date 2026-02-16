package com.inlaco.crewmgrservice.infrastructure.serialization.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.upload.application.enums.UploadStrategy;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadFactory;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileDeserializer extends JsonDeserializer<Asset> {

  private final UploadFactory uploadFactory;

  @Override
  public Asset deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    JsonNode node = p.getCodec().readTree(p);
    if (node == null || node.isEmpty()) {
      return null;
    }

    String assetId = node.get("assetId").asText();
    UploadStrategy strategy = p.getCodec().treeToValue(node.get("strategy"), UploadStrategy.class);
    return uploadFactory.metadata(strategy, assetId);
  }
}
