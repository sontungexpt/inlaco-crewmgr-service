package com.inlaco.crewmgrservice.infrastructure.config.jackson;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr353.JSR353Module;
import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.infrastructure.serialization.deserializer.FileDeserializer;
import com.inlaco.crewmgrservice.infrastructure.serialization.serializer.FileSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class JacksonConfig {

  private final FileSerializer fileSerializer;
  private final FileDeserializer fileDeserializer;

  @Bean
  @Primary
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();

    SimpleModule module = new SimpleModule();
    module.addSerializer(File.class, fileSerializer);
    module.addDeserializer(File.class, fileDeserializer);
    mapper.registerModule(module);

    mapper
        .configOverride(String.class)
        .setSetterInfo(JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY));

    // DefaultSerializerProvider.Impl sp = new DefaultSerializerProvider.Impl();
    // sp.setNullValueSerializer(new NullToEmptyStringSerializer());

    return mapper
        // .setSerializerProvider(sp)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        // https://stackoverflow.com/questions/45662820/how-to-set-format-of-string-for-java-time-instant-using-objectmapper
        .registerModule(new JavaTimeModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        // https://cassiomolin.com/programming/using-http-patch-in-spring/
        .registerModule(new JSR353Module());
  }
}
