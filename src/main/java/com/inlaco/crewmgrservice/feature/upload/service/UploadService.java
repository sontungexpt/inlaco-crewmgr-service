package com.inlaco.crewmgrservice.feature.upload.service;

import com.inlaco.crewmgrservice.feature.upload.dto.UploadOptions;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadToken;
import java.util.List;

public interface UploadService {

  UploadOptions getUploadOptions();

  void uploadFile(List<UploadToken> uploadTokens);
}
