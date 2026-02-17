package com.inlaco.crewmgrservice.shared.mapstruct.mapper;

import com.inlaco.crewmgrservice.shared.application.port.in.AssetUrlResolver;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import com.inlaco.crewmgrservice.shared.objectvalue.AssetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AssetResponseMapper {

  private final AssetUrlResolver urlResolver;

  public AssetResponse map(Asset asset) {
    return AssetResponse.builder()
        .type(asset.getType())
        .publicId(asset.getPublicId())
        .displayName(asset.getDisplayName())
        .resourceType(asset.getResourceType())
        .size(asset.getSize())
        .format(asset.getFormat())
        .url(urlResolver.resolve(asset))
        .build();
  }
}
