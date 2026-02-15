package com.inlaco.crewmgrservice.feature.upload.domain.model;

import java.util.HashMap;
import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CloudinarySignParams {

  private final Map<String, Object> signParams =
      new HashMap<>() {
        {
          put("timestamp", System.currentTimeMillis() / 1000);
        }
      };

  public Map<String, Object> toMap() {
    return new HashMap<>(signParams);
  }

  public CloudinarySignParams timestamp(Long timestamp) {
    signParams.put("timestamp", timestamp);
    return this;
  }

  public CloudinarySignParams folder(String folder) {
    signParams.put("folder", folder);
    signParams.put("asset_folder", folder);
    return this;
  }

  public CloudinarySignParams tags(String tags) {
    signParams.put("tags", tags);
    return this;
  }

  public CloudinarySignParams publicId(String publicId) {
    signParams.put("public_id", publicId);
    return this;
  }

  public CloudinarySignParams overwrite(Boolean overwrite) {
    signParams.put("overwrite", overwrite);
    return this;
  }

  public CloudinarySignParams displayName(String displayName) {
    signParams.put("display_name", displayName);
    return this;
  }

  public CloudinarySignParams type(String type) {
    signParams.put("type", type);
    return this;
  }

  public CloudinarySignParams accessMode(String mode) {
    signParams.put("access_mode", mode);
    return this;
  }

  public CloudinarySignParams resourceType(String value) {
    signParams.put("resource_type", value);
    return this;
  }

  public CloudinarySignParams param(String key, Object value) {
    signParams.put(key, value);
    return this;
  }
}
