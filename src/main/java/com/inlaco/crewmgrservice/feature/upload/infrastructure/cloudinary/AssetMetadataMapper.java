package com.inlaco.crewmgrservice.feature.upload.infrastructure.cloudinary;

import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.inlaco.crewmgrservice.feature.upload.domain.model.AssetMetadata;
import java.time.Instant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssetMetadataMapper {
  default AssetMetadata toAssetMetadata(ApiResponse response) {
    return AssetMetadata.builder()
        .assetId(ObjectUtils.asString(response.get("asset_id")))
        .type(ObjectUtils.asString(response.get("type")))
        .publicId(ObjectUtils.asString(response.get("public_id")))
        .displayName(ObjectUtils.asString(response.get("display_name")))
        .resourceType(ObjectUtils.asString(response.get("resource_type")))
        .format(ObjectUtils.asString(response.get("format")))
        .version(ObjectUtils.asInteger(response.get("version"), 0))
        .size(ObjectUtils.asLong(response.get("bytes"), 0L))
        .width(ObjectUtils.asInteger(response.get("width"), 0))
        .height(ObjectUtils.asInteger(response.get("height"), 0))
        .assetFolder(ObjectUtils.asString(response.get("asset_folder")))
        .url(ObjectUtils.asString(response.get("url")))
        .secureUrl(ObjectUtils.asString(response.get("secure_url")))
        .createdAt(parseInstant(response.get("created_at")))
        .build();
  }

  default Instant parseInstant(Object value) {
    return value != null ? Instant.parse(ObjectUtils.asString(value)) : null;
  }
}
