package com.inlaco.crewmgrservice.infrastructure.serialization.serializer.slug;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.inlaco.crewmgrservice.domain.model.Sluggable;
import com.inlaco.crewmgrservice.shared.slug.SlugDefinition;
import com.inlaco.crewmgrservice.shared.slug.SlugProcessor;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SluggableSerializer extends JsonSerializer<Sluggable> {

  private final SlugProcessor slugProcessor;
  private final SlugDefinition slugDefinition;

  public SluggableSerializer(SlugProcessor slugProcessor, SlugDefinition slugDefinition) {
    this.slugProcessor = slugProcessor;
    this.slugDefinition = slugDefinition;
  }

  // prevent loop
  private Map<Object, Boolean> serialized = new ConcurrentHashMap<>();

  @Override
  public void serialize(Sluggable value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {

    try {
      if (!serialized.containsKey(value)) {
        serialized.put(value, true);
        slugProcessor.generate(value, slugDefinition);
        log.info("[SlugableSerializer]: generating slug for {}", value);
        System.out.println("creating");

        // value.markSlugProcessed();
        gen.writeObject(value);
      } else {
        System.out.println("created");
        serialized.remove(value);
      }
    } catch (Exception ex) {
      log.error(
          "SlugableSerializer: failed to generate slug for {}", value.getClass().getName(), ex);
      throw ex;
    }

    log.trace(
        "SlugableSerializer: serialization completed for {}", value.getClass().getSimpleName());
  }
}
