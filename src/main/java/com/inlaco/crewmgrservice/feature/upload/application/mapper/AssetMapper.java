package com.inlaco.crewmgrservice.feature.upload.application.mapper;

import com.inlaco.crewmgrservice.feature.upload.domain.model.AssetMetadata;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssetMapper {

  Asset toAsset(AssetMetadata metadata);

  // default Asset toAsset(AssetMetadata metadata) {
  //   return Asset.builder()
  //       .type(metadata.getType())
  //       .assetId(metadata.getAssetId())
  //       .publicId(metadata.getPublicId())
  //       .displayName(metadata.getDisplayName())
  //       .resourceType(metadata.getResourceType())
  //       .size(metadata.getSize())
  //       .format(metadata.getFormat())
  //       .build();
  // }

}
