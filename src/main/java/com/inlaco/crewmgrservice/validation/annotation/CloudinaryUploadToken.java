package com.inlaco.crewmgrservice.validation.annotation;

import com.inlaco.crewmgrservice.validation.validator.CloudinaryUploadTokenValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CloudinaryUploadTokenValidator.class)
@Documented
public @interface CloudinaryUploadToken {
  String message() default "The upload token not found.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
