package com.inlaco.crewmgrservice.feature.post.enums;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public enum PostType {
  @FieldNameConstants.Include
  BASIC,
  @FieldNameConstants.Include
  RECRUITMENT,
  @FieldNameConstants.Include
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
