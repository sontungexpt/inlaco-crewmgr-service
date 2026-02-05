package com.inlaco.crewmgrservice.infrastructure.web.validation.matchpassword;

import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.IMatchPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MatchPasswordValidator implements ConstraintValidator<MatchPassword, IMatchPassword> {

  private Boolean allowedEmpty;

  @Override
  public void initialize(MatchPassword constraintAnnotation) {
    allowedEmpty = constraintAnnotation.allowedEmpty();
  }

  @Override
  public boolean isValid(IMatchPassword request, ConstraintValidatorContext context) {
    String password = request.getPasswordToMatch();
    String confirmPassword = request.getMatchingPassword();

    if (allowedEmpty && !StringUtils.hasText(password) && !StringUtils.hasText(confirmPassword))
      return true;

    return password.equals(confirmPassword);
  }
}
