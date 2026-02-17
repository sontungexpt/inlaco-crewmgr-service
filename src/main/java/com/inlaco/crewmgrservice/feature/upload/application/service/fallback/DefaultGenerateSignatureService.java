package com.inlaco.crewmgrservice.feature.upload.application.service.fallback;

import com.inlaco.crewmgrservice.feature.upload.application.port.in.SignatureGenerator;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.feature.upload.domain.model.CloudinarySignedParams;
import com.inlaco.crewmgrservice.feature.upload.domain.model.UploadContext;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultGenerateSignatureService {

  private final SignatureGenerator signatureGenerator;

  public Map<String, Object> signParams(AssetType type, UploadContext context) {
    return CloudinarySignedParams.params()
        .folder(type.name().toLowerCase())
        .sign(params -> signatureGenerator.generate(params))
        .toMap();
  }
}
