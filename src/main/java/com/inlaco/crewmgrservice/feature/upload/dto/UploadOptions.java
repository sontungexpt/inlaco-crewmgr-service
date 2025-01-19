package com.inlaco.crewmgrservice.feature.upload.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadOptions {

  @Schema(description = "Upload token")
  private String token;

  @Schema(description = "API options for upload")
  private Map<String, Object> apiOptions;
}
