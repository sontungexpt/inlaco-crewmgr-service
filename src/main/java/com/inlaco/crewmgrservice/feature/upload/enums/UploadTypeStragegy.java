package com.inlaco.crewmgrservice.feature.upload.enums;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public enum UploadTypeStragegy {
  @FieldNameConstants.Include
  SAILOR_PROFILE,

  @FieldNameConstants.Include
  CANDIDATE_PROFILE,
}
