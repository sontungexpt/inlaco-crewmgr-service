package com.inlaco.crewmgrservice.feature.upload.application.mapper;

import com.inlaco.crewmgrservice.feature.upload.domain.model.AssetMetadata;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssetMapper {

  Asset toAsset(AssetMetadata metadata);

  default Asset toAsset(String assetId) {
    return Asset.builder().assetId(assetId).build();
  }
}
