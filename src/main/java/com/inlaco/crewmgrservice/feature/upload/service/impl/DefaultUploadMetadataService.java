package com.inlaco.crewmgrservice.feature.upload.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.inlaco.crewmgrservice.common.model.File;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultUploadMetadataService {

  private final Cloudinary cloudinary;

  public File buildMetadata(ApiResponse response) {
    File metadata =
        File.builder()
            .publicId((String) response.get("public_id"))
            .url((String) response.get("secure_url"))
            .displayName((String) response.get("display_name"))
            .resourceType((String) response.get("resource_type"))
            .format((String) response.get("format"))
            .bytes(((Number) response.get("bytes")).longValue())
            .uploadedAt(Instant.parse((String) response.get("created_at")))
            .build();
    return metadata;
  }

  public File metadata(String publicId) {
    try {
      ApiResponse response = cloudinary.api().resource(publicId, ObjectUtils.asMap());
      if (response == null || response.isEmpty()) {
        return null;
      }
      return buildMetadata(response);
    } catch (Exception e) {
      log.info("Error getting resume metadata", e);
      return null;
    }
  }
}
