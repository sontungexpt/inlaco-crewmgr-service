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
    if (!value.getTimeFrames().isEmpty()) {
      for (var pair : value.getTimeFrames()) {
        if (!pair.getFirst().isBefore(pair.getSecond())) {
          String msg =
              "start date: "
                  + pair.getFirst()
                  + " is not before "
                  + "end date: "
                  + pair.getSecond();
          context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();

          return false;
        }
      }

      context.disableDefaultConstraintViolation();
    }
    return true;
  }
}
