package com.inlaco.crewmgrservice.feature.user.service.impl;

import com.inlaco.crewmgrservice.feature.upload.dto.UploadOptions;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadToken;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadType;
import com.inlaco.crewmgrservice.feature.upload.enums.UploadTypeStragegy;
import com.inlaco.crewmgrservice.feature.upload.service.UploadServiceStragegy;
import com.inlaco.crewmgrservice.feature.user.service.SailorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service(UploadTypeStragegy.Fields.SAILOR_PROFILE)
public class SailorProfileUploadStragegy implements UploadServiceStragegy {

  private final SailorService sailorService;

  @Override
  public UploadOptions getUploadOptions(UploadType type, String id) {
    throw new UnsupportedOperationException("Unimplemented method 'uploadFile'");
  }

  @Override
  public void uploadFile(UploadType type, List<UploadToken> uploadTokens) {
    throw new UnsupportedOperationException("Unimplemented method 'uploadFile'");
  }
}
