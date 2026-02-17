package com.inlaco.crewmgrservice.feature.upload.infrastructure.cloudinary;

import com.cloudinary.Cloudinary;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.SignatureGenerator;
import com.inlaco.crewmgrservice.infrastructure.config.cloud.CloudinaryProperties;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CloudinarySignatureGenerator implements SignatureGenerator {

  private final Cloudinary cloudinary;
  private final CloudinaryProperties cloudinaryProperties;

  @Override
  public String generate(Map<String, Object> params) {
    return cloudinary.apiSignRequest(params, cloudinaryProperties.getApiSecret(), 2);
  }
}
