package com.inlaco.crewmgrservice.feature.upload.dto;

import lombok.Data;

@Data
public class UploadType {

  private String type;

  private UploadType nestedType;
}
