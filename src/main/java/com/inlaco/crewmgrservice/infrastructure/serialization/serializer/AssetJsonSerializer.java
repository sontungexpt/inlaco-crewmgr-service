package com.inlaco.crewmgrservice.infrastructure.serialization.serializer;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.inlaco.crewmgrservice.shared.application.port.in.AssetUrlResolver;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AssetJsonSerializer extends JsonSerializer<Asset> {

  private final AssetUrlResolver urlResolver;
  private final ObjectMapper mapper = JsonMapper.builder().build();

  @Override
  public void serialize(Asset value, JsonGenerator gen, SerializerProvider provider)
      throws JsonGenerationException, IOException {
    if (value == null) {
      gen.writeNull();
      return;
    }

    // convert object -> tree (keeps all current fields)
    var node = mapper.valueToTree(value);

    // only operate if node is object
    if (node instanceof ObjectNode obj) {

      // remove assetId field from serialized output
      obj.remove("assetId");

      // try to build url from publicId; fail-safe (don't break serialization)
      String publicId = value.publicId();
      if (publicId != null && !publicId.isBlank()) {
        String url = urlResolver.resolve(value);
        if (url != null) {
          obj.put("url", url);
        }
      }
    }

    // write the modified tree
    gen.writeTree(node);
  }
}
