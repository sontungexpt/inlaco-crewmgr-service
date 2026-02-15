package com.inlaco.crewmgrservice.feature.upload.application.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.inlaco.crewmgrservice.common.model.Asset;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultUploadMetadataService {

  private final Cloudinary cloudinary;

  public Asset buildMetadata(ApiResponse response) {
    Asset metadata =
        Asset.builder()
            .publicId((String) response.get("public_id"))
            .assetId((String) response.get("asset_id"))
            .displayName((String) response.get("display_name"))
            .resourceType((String) response.get("resource_type"))
            .format((String) response.get("format"))
            .bytes(((Number) response.get("bytes")).longValue())
            .uploadedAt(Instant.parse((String) response.get("created_at")))
            .build();
    return metadata;
  }

  public Asset metadata(String assetId) {
    try {
      ApiResponse response = cloudinary.api().resourceByAssetID(assetId, ObjectUtils.asMap());
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
