package com.inlaco.crewmgrservice.feature.upload.domain.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class CloudinarySignedParams {

  private final Map<String, Object> params;

  private CloudinarySignedParams(Map<String, Object> params) {
    this.params = Collections.unmodifiableMap(params);
  }

  public Map<String, Object> toMap() {
    return params;
  }

  // ==========================
  // BUILDER
  // ==========================
  public static Builder params() {
    return new Builder();
  }

  public static class Builder {

    private final Map<String, Object> params = new HashMap<>();

    private Builder() {
      params.put("timestamp", System.currentTimeMillis() / 1000);
    }

    public Builder timestamp(Long timestamp) {
      params.put("timestamp", timestamp);
      return this;
    }

    public Builder folder(String folder) {
      params.put("folder", folder);
      params.put("asset_folder", folder);
      return this;
    }

    public Builder tags(String tags) {
      params.put("tags", tags);
      return this;
    }

    public Builder publicId(String publicId) {
      params.put("public_id", publicId);
      return this;
    }

    public Builder overwrite(Boolean overwrite) {
      params.put("overwrite", overwrite);
      return this;
    }

    public Builder displayName(String displayName) {
      params.put("display_name", displayName);
      return this;
    }

    public Builder type(String type) {
      params.put("type", type);
      return this;
    }

    public Builder accessMode(String mode) {
      params.put("access_mode", mode);
      return this;
    }

    public Builder resourceType(String value) {
      params.put("resource_type", value);
      return this;
    }

    public Builder param(String key, Object value) {
      params.put(key, value);
      return this;
    }

    public CloudinarySignedParams sign(Function<Map<String, Object>, String> signer) {
      Map<String, Object> snapshot = new HashMap<>(params);
      String signature = signer.apply(snapshot);
      params.put("signature", signature);
      return new CloudinarySignedParams(params);
    }
  }
}
