package com.inlaco.crewmgrservice.shared.objectvalue;

import lombok.Builder;

@Builder
public record Asset(
    String type,
    String assetId,
    String publicId,
    String displayName,
    String resourceType,
    Long size,
    String format) {}

// @Getter
// @Builder
// @AllArgsConstructor
// public class Asset {
//   private final String type;
//   private final String assetId;
//   private final String publicId;
//   private final String displayName;
//   private final String resourceType;
//   private final Long size;
//   private final String format;
// }
