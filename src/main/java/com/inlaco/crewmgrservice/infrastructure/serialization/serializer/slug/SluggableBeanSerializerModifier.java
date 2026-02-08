package com.inlaco.crewmgrservice.infrastructure.serialization.serializer.slug;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.inlaco.crewmgrservice.domain.model.Sluggable;
import com.inlaco.crewmgrservice.shared.slug.domain.SlugDefinition;
import com.inlaco.crewmgrservice.shared.slug.domain.SlugProcessor;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SluggableBeanSerializerModifier extends BeanSerializerModifier {

  private final SlugProcessor slugProcessor;
  private final SlugDefinition slugDefinition;

  public SluggableBeanSerializerModifier(
      SlugProcessor slugProcessor, SlugDefinition slugDefinition) {
    this.slugProcessor = slugProcessor;
    this.slugDefinition = slugDefinition;
  }

  @Override
  public JsonSerializer<?> modifySerializer(
      SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {

    if (!Sluggable.class.isAssignableFrom(beanDesc.getBeanClass())) {
      return serializer;
    }

    final JsonSerializer<Object> delegate = (JsonSerializer<Object>) serializer;

    return new JsonSerializer<Object>() {
      @Override
      public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
          throws IOException {

        slugProcessor.generate(value, slugDefinition);

        delegate.serialize(value, gen, serializers);
      }
    };
  }
}
