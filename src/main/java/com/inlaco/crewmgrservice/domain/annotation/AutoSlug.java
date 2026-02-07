package com.inlaco.crewmgrservice.domain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoSlug {

  String[] fields();

  boolean fromUniqueField() default false;

  boolean unique() default true;

  String separator() default "-";

  UpdateStrategy updateStrategy() default UpdateStrategy.ON_VALUE_CHANGE;

  enum UpdateStrategy {
    ON_DOCUMENT_SAVE,
    ON_VALUE_CHANGE,
    NEVER_UPDATE
  }
}
