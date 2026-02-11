package com.inlaco.crewmgrservice.infrastructure.web.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

  Pattern pattern;

  @Override
  public void initialize(StrongPassword constraintAnnotation) {
    this.pattern = Pattern.compile(constraintAnnotation.regexp());
  }

  @Override
  public boolean isValid(String string, ConstraintValidatorContext context) {
    if (!StringUtils.hasText(string)) return false;
    else if (pattern.matcher(string).matches()) return true;
    return false;
  }
}
