package com.inlaco.crewmgrservice.feature.upload.dto;

import java.util.List;
import lombok.Data;

@Data
public class UploadRequest {

  private UploadType type;

  private List<UploadToken> tokens;
}
