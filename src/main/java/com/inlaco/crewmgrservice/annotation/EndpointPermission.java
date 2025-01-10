package com.inlaco.crewmgrservice.annotation;

import com.inlaco.crewmgrservice.endpoint.APIEndpointName;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EndpointPermission {

  @AliasFor("endpoint")
  APIEndpointName[] value();

  @AliasFor("value")
  APIEndpointName[] endpoint();
}
