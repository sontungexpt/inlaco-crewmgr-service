package com.inlaco.crewmgrservice.feature.upload.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadOptions;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadToken;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadType;
import com.inlaco.crewmgrservice.feature.upload.repository.UploadTokenRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

@RequiredArgsConstructor
public abstract class UploadServiceStragegy {

  protected final UploadTokenRepository uploadTokenRepository;
  protected final CloudinaryService cloudinaryService;

  public String randomToken(Object id) {
    String token = NanoIdUtils.randomNanoId();
    uploadTokenRepository.save(token, id);
    return token;
  }

  public abstract UploadOptions getUploadOptions(UploadType type, @Nullable String id);

  public abstract void uploadFile(List<UploadToken> uploadTokens, @Nullable UploadType nestedType);
}
