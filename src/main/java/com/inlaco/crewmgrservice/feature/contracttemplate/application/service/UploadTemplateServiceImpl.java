package com.inlaco.crewmgrservice.feature.contracttemplate.application.service;

import com.inlaco.crewmgrservice.feature.upload.application.enums.UploadStrategy;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.CloudinaryService;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadServiceStrategy;
import com.inlaco.crewmgrservice.feature.upload.application.service.DefaultUploadMetadataService;
import com.inlaco.crewmgrservice.feature.upload.domain.model.CloudinarySignParams;
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
