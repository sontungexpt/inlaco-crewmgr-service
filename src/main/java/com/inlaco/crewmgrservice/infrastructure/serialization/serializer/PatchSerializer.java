package com.inlaco.crewmgrservice.infrastructure.serialization.serializer;

import com.inlaco.crewmgrservice.shared.application.model.Patch;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class PatchSerializer extends ValueSerializer<Patch> {

  @Override
  public Class<Patch> handledType() {
    return Patch.class;
  }

  @Override
  public void serialize(Patch value, JsonGenerator gen, SerializationContext ctxt)
      throws JacksonException {

    if (value == null) {
      gen.writeNull();
      return;
    }

    if (value instanceof Patch.Unchanged<?>) {
      gen.writeString("UNCHANGED");
      return;
    }

    if (value instanceof Patch.Updated<?> updated) {
      Object actual = updated.value();
      if (actual == null) {
        gen.writeNull();
      } else {
        ctxt.findValueSerializer(actual.getClass()).serialize(actual, gen, ctxt);
      }
    }
  }
}
