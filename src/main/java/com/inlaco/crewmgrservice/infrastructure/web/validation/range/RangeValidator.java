package com.inlaco.crewmgrservice.infrastructure.web.validation.range;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class RangeValidator implements ConstraintValidator<Range, double[]> {

  @Override
  public boolean isValid(double[] value, ConstraintValidatorContext context) {
    if (value == null || value.length != 2) {
      context
          .buildConstraintViolationWithTemplate("Value can not be null and length must equal 2")
          .addConstraintViolation()
          .disableDefaultConstraintViolation();
      return false;
    } else if (value[0] <= 0) {
      context
          .buildConstraintViolationWithTemplate("First value in range must be greater than 0")
          .addConstraintViolation()
          .disableDefaultConstraintViolation();

      return false;
    } else if (value[1] <= 0) {
      context
          .buildConstraintViolationWithTemplate("Second value in range must be greater than 0")
          .addConstraintViolation()
          .disableDefaultConstraintViolation();
      return false;
    } else if (value[0] > value[1]) {
      context
          .buildConstraintViolationWithTemplate("First value must be greater than secong value")
          .addConstraintViolation()
          .disableDefaultConstraintViolation();
      return false;
    }

    return true;
  }
}
