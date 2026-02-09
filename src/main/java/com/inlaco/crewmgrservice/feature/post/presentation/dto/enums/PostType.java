package com.inlaco.crewmgrservice.feature.post.presentation.dto.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Schema(description = "Post type")
public enum PostType {
  @FieldNameConstants.Include
  @Schema(description = "Basic post type")
  NEWS,
  @FieldNameConstants.Include
  @Schema(description = "Recruitment post type")
  RECRUITMENT,
  @FieldNameConstants.Include
  @Schema(description = "Event post type")
  EVENT,
  ;

  public static PostType fromString(String type) {
    for (PostType postType : PostType.values()) {
      if (postType.name().equalsIgnoreCase(type)) {
        return postType;
      }
    }
    return null;
  }

  public static boolean isValid(String type) {
    return fromString(type) != null;
  }
}
