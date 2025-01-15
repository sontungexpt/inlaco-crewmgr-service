package com.inlaco.crewmgrservice.feature.upload.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UploadOptions {

  @Schema(description = "Upload token")
  private String token;

  @Schema(description = "Upload type")
  private String type;

  @Schema(description = "API options for upload")
  private Map<String, Object> apiOptions;
}
