package com.inlaco.crewmgrservice.feature.upload.service;

import com.inlaco.crewmgrservice.feature.upload.dto.UploadOptions;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadToken;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadType;
import java.util.List;

public interface UploadServiceStragegy {

  UploadOptions getUploadOptions(String id, UploadType type);

  void uploadFile(List<UploadToken> uploadTokens);
}
