package com.inlaco.crewmgrservice.validation.validator;

import com.inlaco.crewmgrservice.common.payload.TimeFrame;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class TimeFrameValidator
    implements ConstraintValidator<
        com.inlaco.crewmgrservice.validation.annotation.TimeFrame, TimeFrame> {

  @Override
  public boolean isValid(TimeFrame value, ConstraintValidatorContext context) {
    if (!value.getStartDate().isBefore(value.getEndDate())) {
      String msg =
          "start date: "
              + value.getStartDate()
              + " is not before "
              + "end date: "
              + value.getEndDate();
      context
          .buildConstraintViolationWithTemplate(msg)
          .addConstraintViolation()
          .disableDefaultConstraintViolation();

      return false;
    }
    return true;
  }
}
