package com.inlaco.crewmgrservice.infrastructure.config.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info =
        @Info(
            title = "Inlaco API",
            version = "1.0",
            summary = "Inlaco Service API",
            contact = @Contact(name = "Inlaco", email = "sontungexpt@gmail.com"),
            description = "Documentation Inlaco Service API v1.0"))
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer")
public class OpenApiConfig {
  public static final String BEARER_AUTH_NAME = "Bearer Authentication";
}
