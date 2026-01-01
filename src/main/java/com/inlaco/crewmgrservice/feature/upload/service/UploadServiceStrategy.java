package com.inlaco.crewmgrservice.feature.upload.service;

import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.upload.enums.UploadStrategy;
import com.inlaco.crewmgrservice.feature.upload.model.CloudinarySignParams;
import com.inlaco.crewmgrservice.feature.upload.service.impl.DefaultUploadMetadataService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public abstract class UploadServiceStrategy {

  protected final CloudinaryService cloudinaryService;
  protected final DefaultUploadMetadataService uploadMetadataService;

  public abstract UploadStrategy getStragegy();

  public Map<String, Object> getUploadOptions(Map<String, String> params) {
    UploadStrategy strategy = getStragegy();
    CloudinarySignParams signParams = new CloudinarySignParams();
    if (strategy != null) {
      signParams.folder(strategy.name().toLowerCase());
    }
    return cloudinaryService.getUploadOptions(signParams);
  }

  public File metadata(String publicId) {
    return uploadMetadataService.metadata(publicId);
  }
}
