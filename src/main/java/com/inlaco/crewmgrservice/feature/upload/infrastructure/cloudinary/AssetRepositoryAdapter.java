package com.inlaco.crewmgrservice.feature.upload.infrastructure.cloudinary;

import com.cloudinary.Cloudinary;
import com.inlaco.crewmgrservice.feature.upload.application.port.out.AssetRepository;
import com.inlaco.crewmgrservice.feature.upload.domain.model.AssetMetadata;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetRepositoryAdapter implements AssetRepository {

  private final Cloudinary cloudinary;
  private final AssetMetadataMapper mapper;

  @Override
  public Optional<AssetMetadata> findByAssetId(String assetId, Map<String, Object> options) {
    try {
      if (assetId == null || assetId.isBlank()) return Optional.empty();
      return Optional.ofNullable(cloudinary.api().resourceByAssetID(assetId, options))
          .map(mapper::toAssetMetadata);
    } catch (Exception e) {
      log.error("Error fetching metadata for assetId {}", assetId, e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteByAssetIds(Iterable<String> assetIds, Map<String, Object> options) {
    try {
      if (assetIds == null) return;
      List<String> validAssetIds =
          StreamSupport.stream(assetIds.spliterator(), false)
              .filter(assetId -> assetId != null && !assetId.isBlank())
              .toList();
      if (validAssetIds.isEmpty()) return;
      cloudinary.api().deleteResourcesByAssetIds(assetIds, options);
    } catch (Exception e) {
      log.error("Error deleting assets {}", assetIds, e);
      throw new RuntimeException(e);
    }
  }
}
