//package com.inlaco.crewmgrservice.feature.apikey.presentation.rest.controller;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/v1/external")
//@SecurityRequirement(name = "apiKey")
//public abstract class BaseExternalApiController {
//
//  @GetMapping("/health")
//  @Operation(summary = "Check API health status",
//             description = "Simple endpoint to verify API key authentication is working")
//  public String healthCheck(Authentication authentication) {
//    return "API is accessible. Authenticated as: " + authentication.getName();
//  }
//
//  /**
//   * Helper method to get current authenticated client name
//   */
//  protected String getCurrentClient(Authentication authentication) {
//    return authentication.getName();
//  }
//
//  /**
//   * Helper method to validate if the current client has access to the requested resource
//   * Override in subclasses to implement custom access control
//   */
//  protected boolean hasAccessToResource(Authentication authentication, String resourceId) {
//    // Default implementation allows access to all authenticated clients
//    // Override in subclasses for resource-specific access control
//    return authentication != null && authentication.isAuthenticated();
//  }
//}
