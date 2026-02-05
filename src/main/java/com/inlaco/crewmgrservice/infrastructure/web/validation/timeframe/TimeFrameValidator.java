package com.inlaco.crewmgrservice.infrastructure.web.validation.timeframe;

import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.TimeFrame;
import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.TimeFrame.Pair;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TimeFrameValidator
    implements ConstraintValidator<
        com.inlaco.crewmgrservice.infrastructure.web.validation.timeframe.TimeFrame, TimeFrame> {

  private boolean isVaild(Pair pair, ConstraintValidatorContext context) {
    if (pair.isRequiredFirst() && pair.getStart() == null) {
      context
          .buildConstraintViolationWithTemplate("Start time is required")
          .addConstraintViolation()
          .disableDefaultConstraintViolation();
      return false;
    } else if (pair.isRequiredSecond() && pair.getEnd() == null) {
      context
          .buildConstraintViolationWithTemplate("End time is required")
          .addConstraintViolation()
          .disableDefaultConstraintViolation();
      return false;
    } else if (pair.getStart() != null && pair.getEnd() != null) {
      return pair.getStart().isBefore(pair.getEnd());
    }
    return true;
  }

  @Override
  public boolean isValid(TimeFrame value, ConstraintValidatorContext context) {
    if (!value.getTimeFrames().isEmpty()) {
      for (var pair : value.getTimeFrames()) {
        log.debug("pair: {}", pair);
        if (!isVaild(pair, context)) {
          return false;
        }
      }
    }
    return true;
  }
}
