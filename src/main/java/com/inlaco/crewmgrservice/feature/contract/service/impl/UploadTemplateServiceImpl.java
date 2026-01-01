package com.inlaco.crewmgrservice.feature.contract.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.inlaco.crewmgrservice.common.model.File;
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

  private final Cloudinary cloudinary;

  public UploadTemplateServiceImpl(
      Cloudinary cloudinary,
      CloudinaryService cloudinaryService,
      DefaultUploadMetadataService uploadMetadataService) {
    super(cloudinaryService, uploadMetadataService);
    this.cloudinary = cloudinary;
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

  @Override
  public File metadata(String publicId) {
    try {
      ApiResponse response =
          cloudinary.api().resource(publicId, ObjectUtils.asMap("resource_type", "raw"));
      if (response == null || response.isEmpty()) {
        return null;
      }
      return uploadMetadataService.buildMetadata(response);
    } catch (Exception e) {
      log.info("Error getting resume metadata", e);
      return null;
    }
  }
}
