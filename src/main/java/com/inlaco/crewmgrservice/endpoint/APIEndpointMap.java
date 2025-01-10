package com.inlaco.crewmgrservice.endpoint;

import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Schema(hidden = true)
public @interface APIEndpointMap {

  APIEndpointName name();

  String displayName();

  String description() default "";
}
