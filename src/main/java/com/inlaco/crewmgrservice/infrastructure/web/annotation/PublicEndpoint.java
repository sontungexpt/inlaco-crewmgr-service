package com.inlaco.crewmgrservice.infrastructure.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark the endpoint as public.
 *
 * <p>{@link ApiEndpointSecurityInspector}
 *
 * <p>{@link Profile}
 *
 * <p>{@link SecurityConfig}
 *
 * <p>{@link JwtAuthenticationFilter}
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PublicEndpoint {

  /**
   * The profiles that are allowed to access the endpoint. You can use hte @Profile annotation to
   * specify the profiles that are allowed to access the endpoint. istead of using the profiles
   * attribute of the @PublicEndpoint annotation.
   */
  String[] profiles() default {};

  AuthMode auth() default AuthMode.PUBLIC;

  public enum AuthMode {
    OPTIONAL,
    PUBLIC
  }
}
