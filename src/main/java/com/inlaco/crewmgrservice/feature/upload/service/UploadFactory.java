package com.inlaco.crewmgrservice.feature.upload.service;

import java.util.Map;
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
}
