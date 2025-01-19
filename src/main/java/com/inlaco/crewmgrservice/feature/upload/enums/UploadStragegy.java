package com.inlaco.crewmgrservice.feature.upload.enums;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public enum UploadStragegy implements IUploadStragegy {
  @FieldNameConstants.Include
  SAILOR,
  @FieldNameConstants.Include
  CANDIDATE,
}
