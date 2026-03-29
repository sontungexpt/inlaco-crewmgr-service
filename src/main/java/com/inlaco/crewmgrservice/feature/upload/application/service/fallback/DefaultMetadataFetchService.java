package com.inlaco.crewmgrservice.feature.upload.application.service.fallback;

import com.inlaco.crewmgrservice.feature.upload.application.port.out.AssetRepository;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.feature.upload.domain.model.AssetMetadata;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultMetadataFetchService {

  private final AssetRepository repository;

  public AssetMetadata fetch(AssetType type, String assetId) {
    log.debug("Fetching metadata for assetId {}", assetId);
    return repository
        .findByAssetId(assetId)
        .orElseThrow(() -> new ResourceNotFoundException(AssetMetadata.class, "assetId", assetId));
  }
}
