package com.inlaco.crewmgrservice.feature.upload.application.port.in;

import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;

public interface AssetDeleteUseCase {

  AssetType supports();

  void delete(Iterable<String> assetIds);
}
