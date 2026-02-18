package com.inlaco.crewmgrservice.infrastructure.serialization.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.inlaco.crewmgrservice.shared.application.model.FieldUpdate;
import java.io.IOException;

public class FieldUpdateSerializer<T> extends JsonSerializer<FieldUpdate<T>> {

  @Override
  public void serialize(FieldUpdate<T> value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    if (value instanceof FieldUpdate.Updated<T> updated) {
      gen.writeObject(updated.value());
    }
  }
}
