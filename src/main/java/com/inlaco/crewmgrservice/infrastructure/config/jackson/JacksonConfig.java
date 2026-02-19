package com.inlaco.crewmgrservice.infrastructure.config.jackson;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr353.JSR353Module;
import com.inlaco.crewmgrservice.infrastructure.serialization.deserializer.PatchDeserializer;
import com.inlaco.crewmgrservice.infrastructure.serialization.serializer.FileSerializer;
import com.inlaco.crewmgrservice.shared.application.model.Patch;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tools.jackson.databind.module.SimpleModule;

@Configuration
@RequiredArgsConstructor
public class JacksonConfig {

  private final FileSerializer fileSerializer;
  private final PatchDeserializer fieldUpdateDeserializer;

  // private final FileDeserializer fileDeserializer;

  @Bean
  @Primary
  public com.fasterxml.jackson.databind.ObjectMapper objectMapperJackson2() {
    var mapper = new com.fasterxml.jackson.databind.ObjectMapper();

    var fileModule = new com.fasterxml.jackson.databind.module.SimpleModule();
    fileModule.addSerializer(Asset.class, fileSerializer);
    // fileModule.addDeserializer(Asset.class, fileDeserializer);
    mapper.registerModule(fileModule);

    mapper
        .configOverride(String.class)
        .setSetterInfo(JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY));

    return mapper
        // .setSerializerProvider(sp)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        // https://stackoverflow.com/questions/45662820/how-to-set-format-of-string-for-java-time-instant-using-objectmapper
        .registerModule(new JavaTimeModule())
        // https://cassiomolin.com/programming/using-http-patch-in-spring/
        .registerModule(new JSR353Module());
  }

  @Bean
  public JsonMapperBuilderCustomizer jsonCustomizerJackson3() {
    return builder ->
        builder.addModules(
            new SimpleModule().addDeserializer(Patch.class, fieldUpdateDeserializer));
  }
}
