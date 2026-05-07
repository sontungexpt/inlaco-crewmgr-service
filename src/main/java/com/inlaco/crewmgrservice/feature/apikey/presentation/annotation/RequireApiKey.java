package com.inlaco.crewmgrservice.feature.apikey.presentation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireApiKey {
  
  /**
   * Description of the API feature for documentation
   */
  String value() default "";
  
  /**
   * Optional feature name for categorization
   */
  String feature() default "";
}
