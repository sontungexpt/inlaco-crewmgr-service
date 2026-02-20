package com.inlaco.crewmgrservice.infrastructure.serialization.serializer;

import com.inlaco.crewmgrservice.shared.application.port.in.AssetUrlResolver;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

@Component
@RequiredArgsConstructor
@Slf4j
public class AssetSerializer extends ValueSerializer<Asset> {

  private final ObjectMapper objectMapper = JsonMapper.builder().build();
  private final AssetUrlResolver urlResolver;

  @Override
  public void serialize(Asset value, JsonGenerator gen, SerializationContext ctxt)
      throws JacksonException {

    if (value == null) {
      gen.writeNull();
      return;
    }

    // convert object -> tree (keeps all current fields)
    var node = objectMapper.valueToTree(value);

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
