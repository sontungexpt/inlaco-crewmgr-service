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
    log.info("Starting metadata fetch for assetId: {}", assetId);
    AssetMetadata metadata =
        repository
            .findByAssetId(assetId)
            .orElseThrow(
                () -> {
                  log.warn("Metadata not found for assetId: {}", assetId);
                  return new ResourceNotFoundException(AssetMetadata.class, "assetId", assetId);
                });
    log.info("Successfully fetched metadata for assetId: {}", assetId);
    return metadata;
  }
}
