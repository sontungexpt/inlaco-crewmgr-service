package com.inlaco.crewmgrservice.feature.upload.application.service.fallback;

import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.feature.upload.domain.model.AssetMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultAssetValidationService {

  public void validate(AssetType type, AssetMetadata metadata) {}
}
