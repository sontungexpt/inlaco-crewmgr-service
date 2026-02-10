package com.inlaco.crewmgrservice.feature.post.domain.enums;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public enum PostType {
  @FieldNameConstants.Include
  NEWS,
  @FieldNameConstants.Include
  RECRUITMENT,
  @FieldNameConstants.Include
  EVENT,
}
