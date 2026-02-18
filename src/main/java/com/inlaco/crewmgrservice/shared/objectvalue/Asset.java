package com.inlaco.crewmgrservice.shared.objectvalue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Asset {
  private final String type;
  private final String assetId;
  private final String publicId;
  private final String displayName;
  private final String resourceType;
  private final Long size;
  private final String format;
}
