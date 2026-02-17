package com.inlaco.crewmgrservice.feature.upload.infrastructure.cloudinary;

import com.cloudinary.Cloudinary;
import com.inlaco.crewmgrservice.feature.upload.application.port.out.AssetMetadataRepository;
import com.inlaco.crewmgrservice.feature.upload.domain.model.AssetMetadata;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetMetadataRepositoryAdapter implements AssetMetadataRepository {

  private final Cloudinary cloudinary;
  private final AssetMetadataMapper mapper;

  @Override
  public Optional<AssetMetadata> findByAssetId(String assetId, Map<String, Object> options) {
    try {
      return Optional.ofNullable(cloudinary.api().resourceByAssetID(assetId, options))
          .map(mapper::toAssetMetadata);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
