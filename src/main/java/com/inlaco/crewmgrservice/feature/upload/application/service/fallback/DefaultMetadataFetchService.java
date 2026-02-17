package com.inlaco.crewmgrservice.feature.upload.application.service.fallback;

import com.inlaco.crewmgrservice.feature.upload.application.port.out.AssetMetadataRepository;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.feature.upload.domain.model.AssetMetadata;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultMetadataFetchService {

  private final AssetMetadataRepository repository;

  public AssetMetadata fetch(AssetType type, String assetId) {
    return repository
        .findByAssetId(assetId)
        .orElseThrow(() -> new ResourceNotFoundException(AssetMetadata.class, "assetId", assetId));
  }
}
