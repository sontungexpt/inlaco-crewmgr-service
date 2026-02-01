package com.inlaco.crewmgrservice.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FilterGroup {
  public static final String DEFAULT_PREFIX = "filter";

  String value() default DEFAULT_PREFIX;
}
