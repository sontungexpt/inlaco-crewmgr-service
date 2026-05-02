package com.inlaco.crewmgrservice.shared.mapstruct.mapper;

import com.inlaco.crewmgrservice.shared.application.port.in.AssetUrlResolver;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import com.inlaco.crewmgrservice.shared.objectvalue.AssetResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AssetResponseMapper {

  private final AssetUrlResolver urlResolver;

  public AssetResponse map(@Nullable Asset asset) {
    if (asset == null) return null;
    return AssetResponse.builder()
        .type(asset.type())
        .publicId(asset.publicId())
        .displayName(asset.displayName())
        .resourceType(asset.resourceType())
        .size(asset.size())
        .format(asset.format())
        .url(urlResolver.resolve(asset))
        .build();
  }
}
