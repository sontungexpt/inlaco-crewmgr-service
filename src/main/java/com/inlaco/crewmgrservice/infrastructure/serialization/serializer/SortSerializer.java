package com.inlaco.crewmgrservice.infrastructure.serialization.serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class SortSerializer extends ValueSerializer<Sort> {

  @Override
  public void serialize(Sort value, tools.jackson.core.JsonGenerator gen, SerializationContext ctxt)
      throws JacksonException {

    if (value == null || value.isUnsorted()) {
      ctxt.findValueSerializer(List.class).serialize(null, gen, ctxt);
      return;
    }

    List<Map<String, Object>> orders = new ArrayList<>();

    for (Sort.Order order : value) {
      orders.add(
          Map.of(
              "property", order.getProperty(),
              "direction", order.getDirection().name(),
              "ignoreCase", order.isIgnoreCase(),
              "nullHandling", order.getNullHandling().name()));
    }

    ctxt.findValueSerializer(List.class).serialize(orders, gen, ctxt);
  }
}
