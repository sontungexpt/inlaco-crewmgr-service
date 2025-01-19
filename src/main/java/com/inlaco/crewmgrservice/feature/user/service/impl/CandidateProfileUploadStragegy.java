package com.inlaco.crewmgrservice.feature.user.service.impl;

import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadOptions;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadToken;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadType;
import com.inlaco.crewmgrservice.feature.upload.enums.UploadStragegy;
import com.inlaco.crewmgrservice.feature.upload.model.CloudinarySignParams;
import com.inlaco.crewmgrservice.feature.upload.repository.UploadTokenRepository;
import com.inlaco.crewmgrservice.feature.upload.service.CloudinaryService;
import com.inlaco.crewmgrservice.feature.upload.service.UploadServiceStragegy;
import com.inlaco.crewmgrservice.feature.user.enums.CandidateUploadStragegy;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import com.inlaco.crewmgrservice.feature.user.service.CandidateService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service(UploadStragegy.Fields.CANDIDATE)
public class CandidateProfileUploadStragegy extends UploadServiceStragegy {

  private final CandidateService candidateService;

  private String RESUME_FOLDER = "candidate-resume";

  public CandidateProfileUploadStragegy(
      CandidateService candidateService,
      CloudinaryService cloudinaryService,
      UploadTokenRepository uploadTokenRepository) {
    super(uploadTokenRepository, cloudinaryService);
    this.candidateService = candidateService;
  }

  public UploadOptions getResumeUploadOptions(String id) {
    if (id != null && !candidateService.existsById(id)) {
      throw new ResourceNotFoundException(CandidateProfile.class, "id", id);
    }

    String token = randomToken(id != null ? id : true);

    CloudinarySignParams signParams =
        new CloudinarySignParams().overwrite(true).folder(RESUME_FOLDER);

    return UploadOptions.builder()
        .token(token)
        .apiOptions(cloudinaryService.getUploadOptions(signParams))
        .build();
  }

  @Override
  public UploadOptions getUploadOptions(UploadType type, String id) {
    var stragegy = type.getStragegy();
    if (stragegy.equals(CandidateUploadStragegy.RESUME)) {
      return getResumeUploadOptions(id);
    } else {
      throw new UnsupportedOperationException("Unimplemented method 'getUploadOptions'");
    }
  }

  @Override
  public void uploadFile(List<UploadToken> uploadTokens, UploadType nestedType) {
    if (nestedType == null) {
      throw new IllegalArgumentException("Nested type is required");
    }

    // for (UploadToken uploadToken : uploadTokens) {
    //   String token = uploadToken.getToken();
    //   String id = (String) getIdFromToken(token);
    //   SailorProfile sailorProfile = sailorService.findSailorProfileById(id);

    //   if (nestedType.getName().equals(CandidateProfile.UploadableType.RESUME.name())) {
    //     sailorProfile.setSocialInsuranceImages(uploadToken.getFiles());
    //   } else if (nestedType
    //       .getName()
    //       .equals(SailorProfile.UploadableType.ACCIDENT_INSURANCE.name())) {
    //     sailorProfile.setAccidentInsuranceImages(uploadToken.getFiles());
    //   } else {
    //     throw new UnsupportedOperationException("Unimplemented method 'uploadFile'");
    //   }

    //   sailorService.saveSailorProfile(sailorProfile);
    // }
  }
}
