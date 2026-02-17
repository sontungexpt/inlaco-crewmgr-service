package com.inlaco.crewmgrservice.feature.upload.application.port.out;

import com.inlaco.crewmgrservice.feature.upload.domain.model.AssetMetadata;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public interface AssetMetadataRepository {

  default Optional<AssetMetadata> findByAssetId(String assetId) {
    return findByAssetId(assetId, Collections.emptyMap());
  }

  Optional<AssetMetadata> findByAssetId(String assetId, Map<String, Object> options);
}
