package com.inlaco.crewmgrservice.feature.contract.service.impl;

import com.inlaco.crewmgrservice.feature.upload.enums.UploadStrategy;
import com.inlaco.crewmgrservice.feature.upload.model.CloudinarySignParams;
import com.inlaco.crewmgrservice.feature.upload.service.CloudinaryService;
import com.inlaco.crewmgrservice.feature.upload.service.UploadServiceStrategy;
import com.inlaco.crewmgrservice.feature.upload.service.impl.DefaultUploadMetadataService;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UploadTemplateServiceImpl extends UploadServiceStrategy {

  public UploadTemplateServiceImpl(
      CloudinaryService cloudinaryService, DefaultUploadMetadataService uploadMetadataService) {
    super(cloudinaryService, uploadMetadataService);
  }

  @Override
  public UploadStrategy getStragegy() {
    return UploadStrategy.CONTRACT_TEMPLATE;
  }

  @Override
  public Map<String, Object> getUploadOptions(Map<String, String> params) {
    return cloudinaryService.getUploadOptions(
        new CloudinarySignParams()
            .displayName(params.get("name"))
            .folder(getStragegy().name().toLowerCase()));
  }
}
