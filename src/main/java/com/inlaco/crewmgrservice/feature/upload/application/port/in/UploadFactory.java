package com.inlaco.crewmgrservice.feature.upload.application.port.in;

import com.inlaco.crewmgrservice.common.model.Asset;
import com.inlaco.crewmgrservice.feature.upload.application.enums.UploadStrategy;
import com.inlaco.crewmgrservice.feature.upload.application.service.DefaultUploadMetadataService;
import com.inlaco.crewmgrservice.feature.upload.domain.model.CloudinarySignParams;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UploadFactory {

  private final CloudinaryService cloudinaryService;
  private final DefaultUploadMetadataService uploadMetadataService;

  private Map<UploadStrategy, UploadServiceStrategy> strategyMap;

  public UploadFactory(
      DefaultUploadMetadataService uploadMetadataService,
      CloudinaryService cloudinaryService,
      List<UploadServiceStrategy> strategies) {
    Map<UploadStrategy, UploadServiceStrategy> map =
        strategies.stream()
            .collect(Collectors.toMap(UploadServiceStrategy::getStragegy, Function.identity()));
    this.strategyMap = map;
    this.uploadMetadataService = uploadMetadataService;
    this.cloudinaryService = cloudinaryService;
  }

  public UploadServiceStrategy getUploadService(UploadStrategy strategy) {
    return strategyMap.get(strategy);
  }

  public Map<String, Object> getUploadOptions(UploadStrategy strategy, Map<String, String> params) {
    UploadServiceStrategy service = getUploadService(strategy);
    if (service == null) {
      return cloudinaryService.getUploadOptions(
          new CloudinarySignParams().folder(strategy.name().toLowerCase()));
    }
    return service.getUploadOptions(params);
  }

  public Asset metadata(UploadStrategy stragegy, String assetId) {
    if (stragegy == null) {
      return uploadMetadataService.metadata(assetId);
    }
    UploadServiceStrategy service = getUploadService(stragegy);
    if (service == null) {
      return uploadMetadataService.metadata(assetId);
    }
    return service.metadata(assetId);
  }
}
