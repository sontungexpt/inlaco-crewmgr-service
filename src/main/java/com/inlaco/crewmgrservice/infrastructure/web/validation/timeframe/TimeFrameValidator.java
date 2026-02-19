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

  @Override
  public boolean isValid(TimeFrame value, ConstraintValidatorContext context) {

    if (value == null || value.getTimeFrames() == null) {
      return true;
    }

    context.disableDefaultConstraintViolation();

    boolean valid = true;

    for (Range range : value.getTimeFrames()) {
      log.debug("Validating range: {}", range);

      if (!validateRange(range, context)) {
        valid = false;
      }
    }

    return valid;
  }

  private boolean validateRange(Range range, ConstraintValidatorContext context) {

    Instant start = range.start();
    Instant end = range.end();
    var policy = range.policy();

    // 1️⃣ Requirement validation
    switch (policy) {
      case START_REQUIRED -> {
        if (start == null) {
          return violation(context, "Start time is required");
        }
      }

      case END_REQUIRED -> {
        if (end == null) {
          return violation(context, "End time is required");
        }
      }

      case BOTH_REQUIRED -> {
        if (start == null || end == null) {
          return violation(context, "Both start and end are required");
        }
      }

      case BOTH_REQUIRED_IF_EITHER_PRESENT -> {
        if ((start != null && end == null) || (start == null && end != null)) {
          return violation(context, "Both start and end must be provided together");
        }
      }

      case NONE -> {
        // no required rule
      }
    }

    // 2️⃣ Chronological validation
    if (start != null && end != null && !start.isBefore(end)) {
      return violation(context, "Start time must be before end time");
    }

    return true;
  }

  private boolean violation(ConstraintValidatorContext context, String message) {

    context.buildConstraintViolationWithTemplate(message).addConstraintViolation();

    return false;
  }
}
