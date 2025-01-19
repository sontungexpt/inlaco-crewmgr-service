package com.inlaco.crewmgrservice.feature.user.service.impl;

import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadOptions;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadToken;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadType;
import com.inlaco.crewmgrservice.feature.upload.enums.UploadTypeStragegy;
import com.inlaco.crewmgrservice.feature.upload.model.CloudinarySignParams;
import com.inlaco.crewmgrservice.feature.upload.repository.UploadTokenRepository;
import com.inlaco.crewmgrservice.feature.upload.service.CloudinaryService;
import com.inlaco.crewmgrservice.feature.upload.service.UploadServiceStragegy;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import com.inlaco.crewmgrservice.feature.user.service.SailorService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service(UploadTypeStragegy.Fields.SAILOR_PROFILE)
public class SailorProfileUploadStragegy extends UploadServiceStragegy {

  private final SailorService sailorService;

  private String ACCIDENT_INSURANCE_FOLDER = "accident-insurance";
  private String SOCIAL_INSURANCE_FOLDER = "social-insurance";

  public SailorProfileUploadStragegy(
      SailorService sailorService,
      CloudinaryService cloudinaryService,
      UploadTokenRepository uploadTokenRepository) {
    super(uploadTokenRepository, cloudinaryService);
    this.sailorService = sailorService;
  }

  public UploadOptions getSocialInsurranceImageUploadOptions(String id) {
    String token = randomToken(id != null ? id : true);

    CloudinarySignParams signParams =
        new CloudinarySignParams().overwrite(true).folder(SOCIAL_INSURANCE_FOLDER);

    return UploadOptions.builder()
        .token(token)
        .apiOptions(cloudinaryService.getUploadOptions(signParams))
        .build();
  }

  public UploadOptions getAccidentInsurranceImageUploadOptions(String id) {
    String token = randomToken(id != null ? id : true);
    CloudinarySignParams signParams =
        new CloudinarySignParams().overwrite(true).folder(ACCIDENT_INSURANCE_FOLDER);

    return UploadOptions.builder()
        .token(token)
        .apiOptions(cloudinaryService.getUploadOptions(signParams))
        .build();
  }

  @Override
  public UploadOptions getUploadOptions(UploadType type, String id) {
    String t = type.getName();
    if (t.equals(SailorProfile.UploadableType.SOCIAL_INSURANCE.name())) {
      return getSocialInsurranceImageUploadOptions(id);
    } else if (t.equals(SailorProfile.UploadableType.ACCIDENT_INSURANCE.name())) {
      return getAccidentInsurranceImageUploadOptions(id);
    } else {
      throw new UnsupportedOperationException("Unimplemented method 'getUploadOptions'");
    }
  }

  private String getIdFromToken(String token) {
    return (String)
        uploadTokenRepository
            .findByToken(token)
            .orElseThrow(() -> new ResourceNotFoundException(UploadToken.class, "token", token));
  }

  @Override
  public void uploadFile(List<UploadToken> uploadTokens, UploadType nestedType) {
    if (nestedType == null) {
      throw new IllegalArgumentException("Nested type is required");
    }

    for (UploadToken uploadToken : uploadTokens) {
      String token = uploadToken.getToken();
      String id = getIdFromToken(token);
      SailorProfile sailorProfile = sailorService.findSailorProfileById(id);

      if (nestedType.getName().equals(SailorProfile.UploadableType.SOCIAL_INSURANCE.name())) {
        sailorProfile.setSocialInsuranceImages(uploadToken.getFiles());
      } else if (nestedType
          .getName()
          .equals(SailorProfile.UploadableType.ACCIDENT_INSURANCE.name())) {
        sailorProfile.setAccidentInsuranceImages(uploadToken.getFiles());
      } else {
        throw new UnsupportedOperationException("Unimplemented method 'uploadFile'");
      }

      sailorService.saveSailorProfile(sailorProfile);
    }
  }
}
