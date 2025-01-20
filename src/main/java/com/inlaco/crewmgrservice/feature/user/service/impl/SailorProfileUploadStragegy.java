package com.inlaco.crewmgrservice.feature.user.service.impl;

import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadOptions;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadToken;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadType;
import com.inlaco.crewmgrservice.feature.upload.enums.IUploadStragegy;
import com.inlaco.crewmgrservice.feature.upload.enums.UploadStragegy;
import com.inlaco.crewmgrservice.feature.upload.model.CloudinarySignParams;
import com.inlaco.crewmgrservice.feature.upload.repository.UploadTokenRepository;
import com.inlaco.crewmgrservice.feature.upload.service.CloudinaryService;
import com.inlaco.crewmgrservice.feature.upload.service.UploadServiceStragegy;
import com.inlaco.crewmgrservice.feature.user.enums.SailorUploadStragegy;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import com.inlaco.crewmgrservice.feature.user.service.SailorService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service(UploadStragegy.Fields.SAILOR)
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
    if (id != null && !sailorService.existsSailorProfileById(id)) {
      throw new ResourceNotFoundException(SailorProfile.class, "id", id);
    }

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
  public UploadOptions getUploadOptions(UploadType nestedType, String id) {
    IUploadStragegy stragegy = nestedType.getStragegy();
    if (stragegy.equals(SailorUploadStragegy.SOCIAL_INSURANCE)) {
      return getSocialInsurranceImageUploadOptions(id);
    } else if (stragegy.equals(SailorUploadStragegy.ACCIDENT_INSURANCE)) {
      return getAccidentInsurranceImageUploadOptions(id);
    } else {
      throw new UnsupportedOperationException("Unimplemented method 'getUploadOptions'");
    }
  }

  @Override
  public void uploadFile(List<UploadToken> uploadTokens, UploadType nestedType) {
    if (nestedType == null) {
      throw new IllegalArgumentException("Nested type is required");
    }

    for (UploadToken uploadToken : uploadTokens) {
      String token = uploadToken.getToken();
      String id = (String) getIdFromToken(token);
      SailorProfile sailorProfile = sailorService.findSailorProfileById(id);

      IUploadStragegy stragegy = nestedType.getStragegy();
      if (stragegy.equals(SailorUploadStragegy.SOCIAL_INSURANCE)) {
        sailorProfile.setSocialInsuranceImages(uploadToken.getFiles());
      } else if (stragegy.equals(SailorUploadStragegy.ACCIDENT_INSURANCE)) {
        sailorProfile.setAccidentInsuranceImages(uploadToken.getFiles());
      } else {
        throw new UnsupportedOperationException("Unimplemented method 'uploadFile'");
      }

      sailorService.saveSailorProfile(sailorProfile);
    }
  }
}
