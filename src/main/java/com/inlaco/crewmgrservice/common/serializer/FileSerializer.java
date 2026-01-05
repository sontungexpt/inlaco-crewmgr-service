package com.inlaco.crewmgrservice.common.serializer;

import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.inlaco.crewmgrservice.common.model.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@JacksonStdImpl
@Component
@RequiredArgsConstructor
public class FileSerializer extends JsonSerializer<File> {

  private final Cloudinary cloudinary;

  @Override
  public void serialize(File value, JsonGenerator gen, SerializerProvider provider)
      throws JsonGenerationException, IOException {
    gen.writeStartObject();

    // ===== field computed =====
    String url =
        cloudinary
            .url()
            .publicId(value.getPublicId())
            .resourceType(value.getResourceType())
            .format(value.getFormat())
            .generate();

    gen.writeStringField("url", url);

    // ===== field default =====
    gen.writeStringField("publicId", value.getPublicId());
    gen.writeStringField("displayName", value.getDisplayName());
    gen.writeStringField("resourceType", value.getResourceType());
    gen.writeStringField("format", value.getFormat());
    gen.writeNumberField("bytes", value.getBytes());
    gen.writeObjectField("uploadedAt", value.getUploadedAt());

    gen.writeEndObject();
  }
}
