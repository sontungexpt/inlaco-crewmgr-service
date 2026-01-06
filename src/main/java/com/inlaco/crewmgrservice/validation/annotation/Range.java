package com.inlaco.crewmgrservice.validation.annotation;

import com.inlaco.crewmgrservice.validation.validator.RangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RangeValidator.class)
@Documented
public @interface Range {

  String message() default
      "The first value should be smaller than the second one, and both should be greater than 0";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
