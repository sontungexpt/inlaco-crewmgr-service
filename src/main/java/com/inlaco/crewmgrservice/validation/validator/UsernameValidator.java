package com.inlaco.crewmgrservice.validation.validator;

import com.inlaco.crewmgrservice.utils.PhoneNumberValidatorUtils;
import com.inlaco.crewmgrservice.validation.annotation.Username;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.constraintvalidators.AbstractEmailValidator;
import org.springframework.stereotype.Component;

@Component
public class UsernameValidator extends AbstractEmailValidator<Username> {

  @Override
  public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
    var length = value.length();
    if (value == null || length < 3) {
      context
          .buildConstraintViolationWithTemplate("Username must be at least 3 characters")
          .addPropertyNode("username")
          .addConstraintViolation()
          .disableDefaultConstraintViolation();
      ;
      return false;
    } else if (PhoneNumberValidatorUtils.validate(value)) {
      return true;
    } else if (isValidEmail(value, context)) {
      return true;
    }
    context
        .buildConstraintViolationWithTemplate("Username must be a valid email or phone number")
        .addPropertyNode("username")
        .addConstraintViolation()
        .disableDefaultConstraintViolation();
    ;
    return false;
  }

  private boolean isValidEmail(CharSequence email, ConstraintValidatorContext context) {
    return super.isValid(email, context);
  }
}
