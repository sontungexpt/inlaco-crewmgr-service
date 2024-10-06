package com.inlaco.crewmgrservice.validation.validator;

import com.inlaco.crewmgrservice.common.payload.IPasswordResetRequest;
import com.inlaco.crewmgrservice.validation.annotation.MatchPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MatchPasswordValidator
    implements ConstraintValidator<MatchPassword, IPasswordResetRequest> {

  private Boolean allowedEmpty;

  @Override
  public void initialize(MatchPassword constraintAnnotation) {
    allowedEmpty = constraintAnnotation.allowedEmpty();
  }

  @Override
  public boolean isValid(IPasswordResetRequest request, ConstraintValidatorContext context) {
    String password = request.getPassword();
    String confirmPassword = request.getConfirmPassword();

    if (allowedEmpty && !StringUtils.hasText(password) && !StringUtils.hasText(confirmPassword))
      return true;

    return password.equals(confirmPassword);
  }
}
