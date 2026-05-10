package com.inlaco.crewmgrservice.feature.upload.application.service;

import com.inlaco.crewmgrservice.feature.upload.application.mapper.AssetMapper;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.AssetDeleteUseCase;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.AssetValidationUseCase;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.GenerateSignatureUseCase;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.MetadataFetchUseCase;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadDispatcher;
import com.inlaco.crewmgrservice.feature.upload.application.service.fallback.DefaultAssetValidationService;
import com.inlaco.crewmgrservice.feature.upload.application.service.fallback.DefaultDeleteService;
import com.inlaco.crewmgrservice.feature.upload.application.service.fallback.DefaultGenerateSignatureService;
import com.inlaco.crewmgrservice.feature.upload.application.service.fallback.DefaultMetadataFetchService;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.feature.upload.domain.model.AssetMetadata;
import com.inlaco.crewmgrservice.feature.upload.domain.model.UploadContext;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UploadDispatcherImpl implements UploadDispatcher {

  private final Map<AssetType, GenerateSignatureUseCase> generateStrategies;
  private final Map<AssetType, MetadataFetchUseCase> metadataStrategies;
  private final Map<AssetType, AssetValidationUseCase> validationStrategies;
  private final Map<AssetType, AssetDeleteUseCase> deleteStrategies;

  private final DefaultGenerateSignatureService defaultGenerateSignatureService;
  private final DefaultMetadataFetchService defaultMetadataFetchService;
  private final DefaultAssetValidationService defaultAssetValidationService;
  private final DefaultDeleteService defaultDeleteService;
  private final AssetMapper assetMapper;

  public UploadDispatcherImpl(
      List<GenerateSignatureUseCase> generateStrategies,
      List<MetadataFetchUseCase> metadataStrategies,
      List<AssetValidationUseCase> validationStrategies,
      List<AssetDeleteUseCase> deleteStrategies,
      DefaultGenerateSignatureService defaultGenerateSignatureService,
      DefaultMetadataFetchService defaultMetadataFetchService,
      DefaultAssetValidationService defaultAssetValidationService,
      DefaultDeleteService defaultDeleteService,
      AssetMapper assetMapper) {
    this.defaultMetadataFetchService = defaultMetadataFetchService;
    this.defaultGenerateSignatureService = defaultGenerateSignatureService;
    this.defaultAssetValidationService = defaultAssetValidationService;
    this.defaultDeleteService = defaultDeleteService;
    this.assetMapper = assetMapper;

    this.generateStrategies = new EnumMap<>(AssetType.class);
    this.metadataStrategies = new EnumMap<>(AssetType.class);
    this.validationStrategies = new EnumMap<>(AssetType.class);
    this.deleteStrategies = new EnumMap<>(AssetType.class);

    generateStrategies.forEach(
        strategy -> this.generateStrategies.put(strategy.supports(), strategy));
    metadataStrategies.forEach(
        strategy -> this.metadataStrategies.put(strategy.supports(), strategy));
    validationStrategies.forEach(
        strategy -> this.validationStrategies.put(strategy.supports(), strategy));
    deleteStrategies.forEach(strategy -> this.deleteStrategies.put(strategy.supports(), strategy));
  }

  @Override
  public Map<String, Object> signParams(AssetType type, UploadContext context) {
    log.debug("Fetching signature generation strategy for asset type: {}", type);
    var service = generateStrategies.get(type);
    if (service != null) return service.signParams(context);
    log.info("Using default signature generation strategy for asset type: {}", type);
    return defaultGenerateSignatureService.signParams(type, context);
  }

  @Override
  public Asset fetch(AssetType type, String assetId) {
    log.debug("Fetching metadata fetch strategy for asset type: {}", type);
    var service = metadataStrategies.get(type);
    if (service != null) return assetMapper.toAsset(service.fetch(assetId));
    log.info("Using default metadata fetch strategy for asset type: {}", type);
    return assetMapper.toAsset(defaultMetadataFetchService.fetch(type, assetId));
  }

  @Override
  public void validate(AssetType type, AssetMetadata metadata) {
    log.debug("Fetching validation strategy for asset type: {}", type);
    var service = validationStrategies.get(type);
    if (service != null) service.validate(metadata);
    else {
      log.info("Using default validation strategy for asset type: {}", type);
      defaultAssetValidationService.validate(type, metadata);
    }
  }

  @Override
  public void delete(AssetType type, List<String> assetIds) {
    log.debug("Fetching delete strategy for asset type: {}", type);
    var service = deleteStrategies.get(type);
    if (service != null) service.delete(assetIds);
    else {
      log.info("Using default delete strategy for asset type: {}", type);
      defaultDeleteService.delete(type, assetIds);
    }
  }

  @Override
  public Asset enrich(AssetType type, Asset asset) {
    if (asset == null || asset.assetId() == null) return null;
    log.debug("Fetching enrich strategy for asset type: {}", type);
    return fetch(type, asset.assetId());
  }
}
