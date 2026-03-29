package com.inlaco.crewmgrservice.feature.upload.application.service.fallback;

import com.inlaco.crewmgrservice.feature.upload.application.port.out.AssetRepository;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultDeleteService {

  private final AssetRepository repository;

  public void delete(AssetType type, String assetId) {
    log.debug("Deleting metadata for assetId {}", assetId);
    repository.deleteByAssetId(assetId);
  }

  public void delete(AssetType type, List<String> assetIds) {
    log.debug("Deleting metadata for assetIds {}", assetIds);
    repository.deleteByAssetIds(assetIds);
  }
}
