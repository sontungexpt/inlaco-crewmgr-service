package com.inlaco.crewmgrservice.feature.upload.domain.model;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AssetMetadata {

  private final String assetId;
  private final String type;

  private final String publicId;
  private final String displayName;

  private final String resourceType;
  private final String format;

  private final Integer version;
  private final Long size;

  private final Integer width;
  private final Integer height;

  private final String assetFolder;

  private final String url;
  private final String secureUrl;

  private final Instant createdAt;
}
