package com.inlaco.crewmgrservice.feature.apikey.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.apikey.application.port.in.ApiKeyUseCase;
import com.inlaco.crewmgrservice.feature.apikey.application.service.ApiKeyGenerationService;
import com.inlaco.crewmgrservice.feature.apikey.domain.exception.ApiKeyNotFoundException;
import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKey;
import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKeyType;
import com.inlaco.crewmgrservice.feature.apikey.presentation.dto.request.CreateApiKeyWithTotpRequest;
import com.inlaco.crewmgrservice.feature.apikey.presentation.dto.response.ApiKeyResponse;
import com.inlaco.crewmgrservice.feature.apikey.presentation.mapper.ApiKeyMapper;
import com.inlaco.crewmgrservice.feature.totp.application.port.in.TotpUseCase;
import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import com.inlaco.crewmgrservice.feature.totp.presentation.dto.response.TotpSetupResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import com.inlaco.crewmgrservice.infrastructure.web.resolver.CurrentUserArgumentResolver;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/api-keys")
@Tag(name = "API Key Management", description = "APIs for managing API keys for external access")
public class ApiKeyManagementController {
  
  private final ApiKeyUseCase apiKeyUseCase;
  private final ApiKeyGenerationService apiKeyGenerationService;
  private final ApiKeyMapper apiKeyMapper;
  private final TotpUseCase totpUseCase;
  
  @PostMapping("/setup-totp")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Setup TOTP for API key creation")
  public ResponseEntity<TotpSetupResponse> setupApiKeyCreationTotp(
      @CurrentUser User user) {
    
    String userId = user.getId();
    String email = user.getUsername();
    String purposeId = "apikey-" + NanoIdUtils.randomNanoId() + "-" + System.currentTimeMillis();
    
    TotpSetupResponse response = totpUseCase.setupTotp(userId, email, TotpSecret.TotpPurpose.API_KEY_CREATION, purposeId);
    
    return ResponseEntity.ok(response);
  }
  
  @PostMapping("/create-with-totp")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Create a new API key with TOTP verification")
  public ResponseEntity<ApiKeyResponse> createApiKeyWithTotp(
      @Valid @RequestBody CreateApiKeyWithTotpRequest request,
      @CurrentUser User user) {
    
    String userId = user.getId();
    
    try {
      // Create API key with TOTP verification
      ApiKey apiKey = apiKeyGenerationService.generateNewWithTotp(request, userId);
      ApiKeyResponse response = apiKeyMapper.toResponse(apiKey);
      
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }
  
  @GetMapping("/{keyId}/secret")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Get API key secret with TOTP verification")
  public ResponseEntity<ApiKeyResponse> getApiKeySecretWithTotp(
      @PathVariable String keyId,
      @RequestParam String totpCode,
      @RequestParam String purposeId,
      @CurrentUser User user) {
    
    String userId = user.getId();
    
    // Verify TOTP using service
    boolean totpValid = apiKeyGenerationService.verifyTotpForSecretViewing(userId, totpCode, purposeId);
    
    if (!totpValid) {
      return ResponseEntity.badRequest().build();
    }
    
    // Get API key with secret
    ApiKey apiKey = apiKeyUseCase.validateApiKey(keyId, null);
    if (apiKey == null) {
      throw new ApiKeyNotFoundException("API key not found");
    }
    
    ApiKeyResponse response = apiKeyMapper.toResponse(apiKey);
    return ResponseEntity.ok(response);
  }
  
  @PostMapping("/{keyId}/setup-secret-totp")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Setup TOTP to view API key secret")
  public ResponseEntity<TotpSetupResponse> setupSecretViewingTotp(
      @PathVariable String keyId,
      @CurrentUser User user) {
    
    String userId = user.getId();
    String email = user.getUsername();
    String purposeId = "secret-" + keyId + "-" + NanoIdUtils.randomNanoId() + "-" + System.currentTimeMillis();
    
    TotpSetupResponse response = totpUseCase.setupTotp(userId, email, TotpSecret.TotpPurpose.SECRET_KEY_VIEWING, purposeId);
    
    return ResponseEntity.ok(response);
  }
  
  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Get all API keys")
  public ResponseEntity<List<ApiKeyResponse>> getAllApiKeys() {
    List<ApiKey> apiKeys = apiKeyUseCase.getAllApiKeys();
    List<ApiKeyResponse> responses = apiKeys.stream()
        .map(apiKeyMapper::toResponse)
        .toList();
    return ResponseEntity.ok(responses);
  }
  
  @GetMapping("/client/{clientName}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Get API keys by client name")
  public ResponseEntity<List<ApiKeyResponse>> getApiKeysByClient(@PathVariable String clientName) {
    List<ApiKey> apiKeys = apiKeyUseCase.getApiKeysByClient(clientName);
    List<ApiKeyResponse> responses = apiKeys.stream()
        .map(apiKeyMapper::toResponse)
        .toList();
    return ResponseEntity.ok(responses);
  }
  
  @GetMapping("/active")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Get all active API keys")
  public ResponseEntity<List<ApiKeyResponse>> getActiveApiKeys() {
    List<ApiKey> apiKeys = apiKeyUseCase.getActiveApiKeys();
    List<ApiKeyResponse> responses = apiKeys.stream()
        .map(apiKeyMapper::toResponse)
        .toList();
    return ResponseEntity.ok(responses);
  }
  
  @GetMapping("/inactive")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Get all inactive API keys")
  public ResponseEntity<List<ApiKeyResponse>> getInactiveApiKeys() {
    List<ApiKey> apiKeys = apiKeyUseCase.getInactiveApiKeys();
    List<ApiKeyResponse> responses = apiKeys.stream()
        .map(apiKeyMapper::toResponse)
        .toList();
    return ResponseEntity.ok(responses);
  }
  
  @GetMapping("/creator/{createdBy}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Get API keys by creator")
  public ResponseEntity<List<ApiKeyResponse>> getApiKeysByCreator(@PathVariable String createdBy) {
    List<ApiKey> apiKeys = apiKeyUseCase.getApiKeysByCreator(createdBy);
    List<ApiKeyResponse> responses = apiKeys.stream()
        .map(apiKeyMapper::toResponse)
        .toList();
    return ResponseEntity.ok(responses);
  }
  
  @PutMapping("/{keyId}/activate")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Activate an API key")
  public ResponseEntity<Void> activateApiKey(@PathVariable String keyId) {
    apiKeyUseCase.activateApiKey(keyId);
    return ResponseEntity.noContent().build();
  }
  
  @PutMapping("/{keyId}/deactivate")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Deactivate an API key")
  public ResponseEntity<Void> deactivateApiKey(@PathVariable String keyId) {
    apiKeyUseCase.deactivateApiKey(keyId);
    return ResponseEntity.noContent().build();
  }
  
  @GetMapping("/validate")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Validate API key credentials")
  public ResponseEntity<Boolean> validateApiKey(
      @RequestParam String keyId,
      @RequestParam String keySecret) {
    boolean isValid = apiKeyUseCase.isApiKeyValid(keyId, keySecret);
    return ResponseEntity.ok(isValid);
  }
}
