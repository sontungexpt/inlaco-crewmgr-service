package com.inlaco.crewmgrservice.shared.objectvalue;

import lombok.Builder;

@Builder
public record AssetResponse(
    String type,
    String publicId,
    String displayName,
    String resourceType,
    Long size,
    String format,
    String url) {

  // compatible old version
  public Long getBytes() {
    return size;
  }
}
