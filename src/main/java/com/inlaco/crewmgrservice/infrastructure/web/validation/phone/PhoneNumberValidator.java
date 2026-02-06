package com.inlaco.crewmgrservice.infrastructure.web.validation.phone;

import com.inlaco.crewmgrservice.shared.constant.PhoneNumberRegexp;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

  private boolean optional;
  private Pattern[] patterns;
  private PhoneNumberRegexp[] regions;

  @Override
  public void initialize(PhoneNumber annotation) {
    this.optional = annotation.optional();
    this.regions = annotation.regions();
    this.patterns =
        Arrays.stream(annotation.regexp())
            .filter(StringUtils::hasText)
            .map(Pattern::compile)
            .toArray(Pattern[]::new);
  }

  @Override
  public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {

    // 1. Empty handling
    if (!StringUtils.hasText(phoneNumber)) {
      return optional;
    }

    // 2. Custom regex patterns
    for (Pattern pattern : patterns) {
      if (pattern.matcher(phoneNumber).matches()) {
        return true;
      }
    }

    // 3. Region-based validation
    for (PhoneNumberRegexp region : regions) {
      if (region.isValid(phoneNumber)) {
        return true;
      }
    }

    return PhoneNumberRegexp.isValidAny(phoneNumber);
  }
}
