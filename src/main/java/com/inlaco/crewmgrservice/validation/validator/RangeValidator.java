package com.inlaco.crewmgrservice.validation.validator;

import com.inlaco.crewmgrservice.validation.annotation.Range;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RangeValidator implements ConstraintValidator<Range, double[]> {

  @Override
  public boolean isValid(double[] value, ConstraintValidatorContext context) {
    if (value == null || value.length != 2) {
      return false;
    }
    return value[0] > 0 && value[1] > 0 && value[0] < value[1];
  }
}
