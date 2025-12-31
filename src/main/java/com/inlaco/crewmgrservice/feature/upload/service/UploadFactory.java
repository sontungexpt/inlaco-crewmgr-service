package com.inlaco.crewmgrservice.feature.upload.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.upload.enums.UploadStrategy;
import com.inlaco.crewmgrservice.feature.upload.model.CloudinarySignParams;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UploadFactory {

  protected final CloudinaryService cloudinaryService;
  private final Cloudinary cloudinary;

  private Map<UploadStrategy, UploadServiceStrategy> strategyMap;

  public UploadFactory(
      Cloudinary cloudinary,
      CloudinaryService cloudinaryService,
      List<UploadServiceStrategy> strategies) {
    Map<UploadStrategy, UploadServiceStrategy> map =
        strategies.stream()
            .collect(Collectors.toMap(UploadServiceStrategy::getStragegy, Function.identity()));
    this.strategyMap = map;
    this.cloudinary = cloudinary;
    this.cloudinaryService = cloudinaryService;
  }

  public UploadServiceStrategy getUploadService(UploadStrategy strategy) {
    return strategyMap.get(strategy);
  }

  public Map<String, Object> getUploadOptions(UploadStrategy strategy, Object payload) {
    UploadServiceStrategy service = getUploadService(strategy);
    if (service == null) {
      return getDefaultUploadOptions(strategy, payload);
    }
    return service.getUploadOptions(payload);
  }

  private Map<String, Object> getDefaultUploadOptions(UploadStrategy strategy, Object payload) {
    return cloudinaryService.getUploadOptions(
        new CloudinarySignParams().folder(strategy.name().toLowerCase()));
  }

  public File metadata(UploadStrategy stragegy, String publicId) {
    UploadServiceStrategy service = getUploadService(stragegy);
    if (service == null) {
      return defaultMetadata(publicId);
    }
    return service.metadata(publicId);
  }

  private File defaultMetadata(String publicId) {
    try {
      ApiResponse data = cloudinary.api().resource(publicId, ObjectUtils.asMap());

      if (data == null || data.isEmpty()) {
        return null;
      }

      File resume =
          File.builder()
              .publicId((String) data.get("public_id"))
              .url((String) data.get("secure_url"))
              .displayName((String) data.get("display_name"))
              .resourceType((String) data.get("resource_type"))
              .format((String) data.get("format"))
              .bytes(((Number) data.get("bytes")).longValue())
              .uploadedAt(Instant.parse((String) data.get("created_at")))
              .build();

      return resume;

    } catch (Exception e) {
      log.error("Error getting resume metadata", e);
      return null;
    }
  }
}
