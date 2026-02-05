package com.inlaco.crewmgrservice.infrastructure.web.validation.timeframe;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeFrameValidator.class)
@Documented
public @interface TimeFrame {
  String message() default "TimeFrame is not valid. Start and end time must be in correct order";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
