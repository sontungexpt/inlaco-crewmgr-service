package com.inlaco.crewmgrservice.feature.upload.application.port.in;

import com.inlaco.crewmgrservice.feature.upload.application.enums.UploadStrategy;
import com.inlaco.crewmgrservice.feature.upload.application.service.DefaultUploadMetadataService;
import com.inlaco.crewmgrservice.feature.upload.domain.model.CloudinarySignParams;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
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

  public Asset metadata(String assetId) {
    return uploadMetadataService.metadata(assetId);
  }
}
