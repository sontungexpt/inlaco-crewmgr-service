package com.inlaco.crewmgrservice.feature.upload.application.port.out;

import com.inlaco.crewmgrservice.feature.upload.domain.model.AssetMetadata;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public interface AssetRepository {

  default Optional<AssetMetadata> findByAssetId(String assetId) {
    return findByAssetId(assetId, Collections.emptyMap());
  }

  Optional<AssetMetadata> findByAssetId(String assetId, Map<String, Object> options);

  default void deleteByAssetId(String assetId) {
    deleteByAssetIds(Collections.singletonList(assetId), Collections.emptyMap());
  }

  default void deleteByAssetIds(Iterable<String> assetIds) {
    deleteByAssetIds(assetIds, Collections.emptyMap());
  }

  void deleteByAssetIds(Iterable<String> assetIds, Map<String, Object> options);
}
