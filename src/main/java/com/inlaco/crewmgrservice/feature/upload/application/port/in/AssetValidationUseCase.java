package com.inlaco.crewmgrservice.feature.upload.application.port.in;

import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.feature.upload.domain.model.AssetMetadata;

public interface AssetValidationUseCase {

  AssetType supports();

  void validate(AssetMetadata metadata);
}
