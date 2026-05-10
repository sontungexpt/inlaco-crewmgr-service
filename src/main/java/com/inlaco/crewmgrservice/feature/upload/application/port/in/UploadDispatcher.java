package com.inlaco.crewmgrservice.feature.upload.application.port.in;

import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.feature.upload.domain.model.AssetMetadata;
import com.inlaco.crewmgrservice.feature.upload.domain.model.UploadContext;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface UploadDispatcher {

  Map<String, Object> signParams(AssetType type, UploadContext context);

  Asset fetch(AssetType type, String assetId);

  Asset enrich(AssetType type, Asset asset);

  void validate(AssetType type, AssetMetadata metadata);

  void delete(AssetType type, List<String> assetIds);

  default void delete(AssetType type, String assetId) {
    delete(type, Collections.singletonList(assetId));
  }
}
