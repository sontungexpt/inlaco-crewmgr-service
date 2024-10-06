package com.inlaco.crewmgrservice.validation.validator;

import com.inlaco.crewmgrservice.common.regexp.PhoneNumberRegexp;
import com.inlaco.crewmgrservice.utils.PhoneNumberValidatorUtils;
import com.inlaco.crewmgrservice.validation.annotation.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

  private String[] regexps;
  private boolean optional;
  private PhoneNumberRegexp[] regions;

  @Override
  public void initialize(PhoneNumber constraintAnnotation) {
    optional = constraintAnnotation.optional();
    regexps = constraintAnnotation.regexp();
    regions = constraintAnnotation.regions();
  }

  @Override
  public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
    if (!StringUtils.hasText(phoneNumber)) {
      return optional;
    } else if (regexps.length > 0) {
      for (String regexp : regexps) {
        if (StringUtils.hasText(regexp) && phoneNumber.matches(regexp)) {
          return true;
        }
      }
      return false;
    } else if (regions.length > 0) {
      for (PhoneNumberRegexp regionRegexp : regions) {
        if (regionRegexp.isValid(phoneNumber)) {
          return true;
        }
      }
    }
    return PhoneNumberValidatorUtils.validate(phoneNumber);
  }
}
