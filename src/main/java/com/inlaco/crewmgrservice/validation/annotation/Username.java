package com.inlaco.crewmgrservice.validation.annotation;

import com.inlaco.crewmgrservice.validation.validator.UsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
@Documented
public @interface Username {
  String message() default "Invalid username";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
