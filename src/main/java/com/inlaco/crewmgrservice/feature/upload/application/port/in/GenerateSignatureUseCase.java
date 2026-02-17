package com.inlaco.crewmgrservice.feature.upload.application.port.in;

import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.feature.upload.domain.model.UploadContext;
import java.util.Map;

public interface GenerateSignatureUseCase {
  AssetType supports();

  Map<String, Object> signParams(UploadContext context);
}
