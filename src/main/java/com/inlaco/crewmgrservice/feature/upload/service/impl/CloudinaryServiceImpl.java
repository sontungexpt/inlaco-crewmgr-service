package com.inlaco.crewmgrservice.feature.upload.service.impl;

import com.cloudinary.Cloudinary;
import com.inlaco.crewmgrservice.config.CloudinaryConfig;
import com.inlaco.crewmgrservice.feature.upload.model.CloudinarySignParams;
import com.inlaco.crewmgrservice.feature.upload.service.CloudinaryService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

  private final Cloudinary cloudinary;

  @Override
  public Map<String, Object> getUploadOptions(Map<String, Object> paramsToSign) {
    paramsToSign = new HashMap<>(paramsToSign);
    Object timestamp = paramsToSign.get("timestamp");
    if (timestamp == null) {
      paramsToSign.put("timestamp", System.currentTimeMillis() / 1000);
    }
    String signature = cloudinary.apiSignRequest(paramsToSign, CloudinaryConfig.API_SECRET, 2);
    paramsToSign.put("signature", signature);
    return paramsToSign;
  }

  @Override
  public Map<String, Object> getUploadOptions(CloudinarySignParams signParams) {
    Map<String, Object> paramsToSign = signParams.toMap();
    return getUploadOptions(paramsToSign);
  }
}
