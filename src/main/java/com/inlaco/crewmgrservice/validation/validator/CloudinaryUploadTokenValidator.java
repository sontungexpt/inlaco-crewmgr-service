package com.inlaco.crewmgrservice.validation.validator;

import com.inlaco.crewmgrservice.common.payload.UploadableFile;
import com.inlaco.crewmgrservice.feature.upload.repository.UploadTokenRepository;
import com.inlaco.crewmgrservice.validation.annotation.CloudinaryUploadToken;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CloudinaryUploadTokenValidator
    implements ConstraintValidator<CloudinaryUploadToken, UploadableFile> {

  private final UploadTokenRepository uploadTokenRepository;

  @Override
  public boolean isValid(UploadableFile file, ConstraintValidatorContext context) {
    if (file == null) {
      return true;
    }

    String uploadToken = file.getUploadToken();
    return uploadTokenRepository.exists(uploadToken);
  }
}
