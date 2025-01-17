package com.inlaco.crewmgrservice.feature.upload.service;

import java.util.Map;

public interface CloudinaryService {

  String generateSignature(Map<String, Object> paramsToSign);

  Map<String, Object> getUploadApiOptions(Map<String, Object> paramsToSign);
}
