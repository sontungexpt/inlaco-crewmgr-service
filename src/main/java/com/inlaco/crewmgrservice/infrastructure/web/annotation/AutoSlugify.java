package com.inlaco.crewmgrservice.infrastructure.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoSlugify {

  String[] fields();

  boolean fromUniqueField() default false;

  boolean unique() default true;

  Separator separator() default Separator.HYPHEN;

  UpdateStrategy updateStrategy() default UpdateStrategy.ON_VALUE_CHANGE;

  enum UpdateStrategy {
    ON_DOCUMENT_SAVE,
    ON_VALUE_CHANGE,
    NEVER_UPDATE
  }

  enum Separator {
    UNDERSCORE("_"),
    HYPHEN("-");

    private final String value;

    Separator(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }
}
