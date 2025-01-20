package com.inlaco.crewmgrservice.feature.upload.service;

import com.inlaco.crewmgrservice.feature.upload.dto.UploadOptions;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadToken;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadType;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UploadFactory {

  private final Map<String, UploadServiceStragegy> uploadServiceMap;

  public UploadServiceStragegy getUploadService(String type) {
    if (uploadServiceMap.containsKey(type)) {
      return uploadServiceMap.get(type);
    }
    throw new IllegalArgumentException("Invalid upload type");
  }

  public UploadOptions getUploadOptions(UploadType type, String id) {
    return getUploadService(type.getStragegy().name()).getUploadOptions(type.getNestedType(), id);
  }

  public void uploadFile(UploadType type, List<UploadToken> uploadTokens) {
    CompletableFuture.runAsync(
        () ->
            getUploadService(type.getStragegy().name())
                .uploadFile(uploadTokens, type.getNestedType()));
  }
}
