package com.inlaco.crewmgrservice.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FilterGroup {
  String value() default "filter";
}
