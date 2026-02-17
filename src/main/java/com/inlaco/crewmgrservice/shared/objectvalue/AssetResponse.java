package com.inlaco.crewmgrservice.shared.objectvalue;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AssetResponse {
  private final String type;
  private final String publicId;
  private final String displayName;
  private final String resourceType;
  private final Long size;
  private final String format;
  private final String url;

  // compatible old version
  public Long getBytes() {
    return size;
  }
}
