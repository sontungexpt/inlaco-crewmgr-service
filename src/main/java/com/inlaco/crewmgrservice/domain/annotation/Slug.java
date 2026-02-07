package com.inlaco.crewmgrservice.domain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Slug {

  String[] fields();

  boolean hyphen() default true;

  boolean lowerCase() default false;
}
