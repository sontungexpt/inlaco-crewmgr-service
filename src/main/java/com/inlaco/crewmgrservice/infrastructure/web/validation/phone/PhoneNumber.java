package com.inlaco.crewmgrservice.infrastructure.web.validation.phone;

import com.inlaco.crewmgrservice.constant.PhoneNumberRegexp;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
@Documented
public @interface PhoneNumber {

  @AliasFor("regions")
  PhoneNumberRegexp[] value() default {};

  String message() default "The phone number is invalid";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  boolean optional() default false;

  String[] regexp() default {};

  @AliasFor("value")
  PhoneNumberRegexp[] regions() default {};
}
