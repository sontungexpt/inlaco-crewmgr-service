package com.inlaco.crewmgrservice.infrastructure.web.validation.timeframe;

import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.TimeFrame;
import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.TimeFrame.Range;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TimeFrameValidator
    implements ConstraintValidator<
        com.inlaco.crewmgrservice.infrastructure.web.validation.timeframe.TimeFrame, TimeFrame> {

  private boolean isVaild(Range range, ConstraintValidatorContext context) {
    Instant start = range.getStart();
    Instant end = range.getEnd();
    if (range.isRequiredStart() && start == null) {
      buildViolation(context, "Start time is required");
      return false;
    }

    if (range.isRequiredEnd() && end == null) {
      buildViolation(context, "End time is required");
      return false;
    }

    if (range.isRequiredBothIfEitherPresent()) {
      if ((start != null && end == null) || (start == null && end != null)) {
        buildViolation(context, "Both start and end must be provided together");
        return false;
      }
    }
    if (start != null && end != null) {
      if (!start.isBefore(end)) {
        buildViolation(context, "Start time must be before end time");
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean isValid(TimeFrame value, ConstraintValidatorContext context) {
    if (value == null || value.getTimeFrames() == null) {
      return true;
    }
    boolean valid = true;
    context.disableDefaultConstraintViolation();
    for (var range : value.getTimeFrames()) {
      log.debug("range: {}", range);

      if (!isVaild(range, context)) {
        valid = false;
      }
    }

    return valid;
  }

  private void buildViolation(ConstraintValidatorContext context, String message) {

    context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
  }
}
