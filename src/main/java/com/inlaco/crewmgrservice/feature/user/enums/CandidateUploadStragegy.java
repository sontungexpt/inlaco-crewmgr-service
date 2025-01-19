package com.inlaco.crewmgrservice.feature.user.enums;

import com.inlaco.crewmgrservice.feature.upload.enums.IUploadStragegy;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public enum CandidateUploadStragegy implements IUploadStragegy {
  @FieldNameConstants.Include
  RESUME
}
