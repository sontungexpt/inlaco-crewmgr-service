package com.inlaco.crewmgrservice.infrastructure.serialization.serializer.slug;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.inlaco.crewmgrservice.domain.model.Sluggable;

public class SluggableBeanSerializerModifier extends BeanSerializerModifier {

  @Override
  public JsonSerializer<?> modifySerializer(
      SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {

    if (Sluggable.class.isAssignableFrom(beanDesc.getBeanClass())) {
      return (JsonSerializer<Object>) serializer;
    }

    return serializer;
  }
}
