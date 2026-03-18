package com.inlaco.crewmgrservice.infrastructure.serialization.serializer;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

@Slf4j
public class SortSerializer extends ValueSerializer<Sort> {

  @Override
  public void serialize(Sort value, tools.jackson.core.JsonGenerator gen, SerializationContext ctxt)
      throws JacksonException {
    if (value == null || value.isUnsorted()) {
      log.debug("Sort is null or unsorted");
      gen.writeBoolean(false);
      return;
    }

    if (log.isDebugEnabled()) {
      String orders =
          value.stream()
              .map(
                  order ->
                      String.format(
                          "{property: %s, direction: %s, ignoreCase: %s, nullHandling: %s}",
                          order.getProperty(),
                          order.getDirection(),
                          order.isIgnoreCase(),
                          order.getNullHandling()))
              .collect(Collectors.joining(", ", "[", "]"));
      log.debug("Sort orders: {}", orders);
    }

    gen.writeBoolean(true);

    // if (value == null || value.isUnsorted()) {
    //   ctxt.findValueSerializer(List.class).serialize(Collections.emptyList(), gen, ctxt);
    //   return;
    // }

    // List<Map<String, Object>> orders = new ArrayList<>();

    // for (Sort.Order order : value) {
    //   orders.add(
    //       Map.of(
    //           "property", order.getProperty(),
    //           "direction", order.getDirection().name(),
    //           "ignoreCase", order.isIgnoreCase(),
    //           "nullHandling", order.getNullHandling().name()));
    // }

    // ctxt.findValueSerializer(List.class).serialize(orders, gen, ctxt);
  }
}
