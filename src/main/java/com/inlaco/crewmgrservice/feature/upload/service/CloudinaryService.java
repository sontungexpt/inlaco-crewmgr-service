package com.inlaco.crewmgrservice.feature.upload.service;

import java.util.HashMap;
import java.util.Map;

public interface CloudinaryService {

  String generateSignature(Map<String, Object> paramsToSign);

  Map<String, Object> getUploadApiOptions(Map<String, Object> paramsToSign);

  default String generateSignature(String folder, String publicId) {
    Map<String, Object> params = new HashMap<>();
    params.put("asset_folder", folder);
    params.put("public_id", publicId);
    return this.generateSignature(params);
  }

  default String generateSignature(String folder, String publicId, String displayName) {
    Map<String, Object> params = new HashMap<>();
    params.put("asset_folder", folder);
    params.put("public_id", publicId);
    params.put("display_name", displayName);
    return this.generateSignature(params);
  }

  default Map<String, Object> getUploadApiOptions(String folder, String publicId) {
    Map<String, Object> options = new HashMap<>();
    options.put("timestamp", System.currentTimeMillis() / 1000L);
    options.put("folder", folder);
    options.put("asset_folder", folder);
    options.put("overwrite", true);
    options.put("public_id", publicId);
    return getUploadApiOptions(options);
  }

  default Map<String, Object> getUploadApiOptions(
      String folder, String publicId, String displayName) {
    Map<String, Object> options = new HashMap<>();
    options.put("timestamp", System.currentTimeMillis() / 1000L);
    options.put("folder", folder);
    options.put("asset_folder", folder);
    options.put("overwrite", true);
    options.put("public_id", publicId);
    options.put("display_name", displayName);
    return getUploadApiOptions(options);
  }
}
