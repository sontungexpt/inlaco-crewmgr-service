package com.inlaco.crewmgrservice.feature.upload.service.impl;

import com.cloudinary.Cloudinary;
import com.inlaco.crewmgrservice.config.CloudinaryConfig;
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
  public String generateSignature(Map<String, Object> paramsToSign) {
    assert paramsToSign != null;
    long timestamp = System.currentTimeMillis() / 1000L;
    try {
      paramsToSign.put("timestamp", timestamp);
    } catch (UnsupportedOperationException e) {
      paramsToSign =
          new HashMap<>(paramsToSign) {
            {
              put("timestamp", timestamp);
            }
          };
    }
    return cloudinary.apiSignRequest(paramsToSign, CloudinaryConfig.API_SECRET);
  }

  @Override
  public Map<String, Object> getUploadApiOptions(Map<String, Object> paramsToSign) {
    String signature = cloudinary.apiSignRequest(paramsToSign, CloudinaryConfig.API_SECRET);
    try {
      paramsToSign.put("signature", signature);
    } catch (UnsupportedOperationException e) {
      paramsToSign =
          new HashMap<>(paramsToSign) {
            {
              put("signature", signature);
            }
          };
    }
    return paramsToSign;
  }
}
