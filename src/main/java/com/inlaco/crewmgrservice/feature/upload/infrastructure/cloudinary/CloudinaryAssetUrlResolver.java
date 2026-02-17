package com.inlaco.crewmgrservice.feature.upload.infrastructure.cloudinary;

import com.cloudinary.Cloudinary;
import com.inlaco.crewmgrservice.shared.application.port.in.AssetUrlResolver;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CloudinaryAssetUrlResolver implements AssetUrlResolver {
  private final Cloudinary cloudinary;

  @Override
  public String resolve(Asset asset) {
    return cloudinary
        .url()
        .publicId(asset.getPublicId())
        .secure(true)
        .resourceType(asset.getResourceType())
        .format(asset.getFormat())
        .generate();
  }
}
