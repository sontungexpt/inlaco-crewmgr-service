//package com.inlaco.crewmgrservice.feature.apikey.presentation.rest.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.inlaco.crewmgrservice.feature.apikey.application.port.in.ApiKeyUseCase;
//import com.inlaco.crewmgrservice.feature.apikey.application.service.ApiKeyService;
//import com.inlaco.crewmgrservice.feature.apikey.domain.exception.ApiKeyNotFoundException;
//import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKey;
//import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKeyType;
//import com.inlaco.crewmgrservice.feature.apikey.presentation.dto.request.CreateApiKeyWithTotpRequest;
//import com.inlaco.crewmgrservice.feature.apikey.presentation.dto.response.ApiKeyResponse;
//import com.inlaco.crewmgrservice.feature.apikey.presentation.mapper.ApiKeyMapper;
//import com.inlaco.crewmgrservice.feature.totp.application.port.in.TotpUseCase;
//import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
//import com.inlaco.crewmgrservice.feature.totp.presentation.dto.response.TotpSetupResponse;
//import com.inlaco.crewmgrservice.feature.user.domain.model.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.time.Instant;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class)
//class ApiKeyManagementControllerTest {
//
//    @Mock
//    private ApiKeyUseCase apiKeyUseCase;
//
//    @Mock
//    private ApiKeyService apiKeyService;
//
//    @Mock
//    private ApiKeyMapper apiKeyMapper;
//
//    @Mock
//    private TotpUseCase totpUseCase;
//
//    @Mock
//    private User mockUser;
//
//    @InjectMocks
//    private ApiKeyManagementController controller;
//
//    private MockMvc mockMvc;
//    private ObjectMapper objectMapper;
//
//    private final String userId = "test-user-id";
//    private final String username = "test@example.com";
//    private final String keyId = "sk_test123";
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
//        objectMapper = new ObjectMapper();
//
//        // Setup mock user
//        when(mockUser.getId()).thenReturn(userId);
//        when(mockUser.getUsername()).thenReturn(username);
//    }
//
//    @Test
//    void setupApiKeyCreationTotp_ShouldReturnTotpSetupResponse() throws Exception {
//        // Given
//        TotpSetupResponse expectedResponse = TotpSetupResponse.builder()
//                .secret("JBSWY3DPEHPK3PXP")
//                .qrCode("data:image/png;base64,test-qr-code")
//                .purposeId("apikey-test123-1683225600000")
//                .purpose(TotpSecret.TotpPurpose.API_KEY_CREATION)
//                .instructions("Scan QR code with authenticator app")
//                .build();
//
//        when(totpUseCase.setupTotp(eq(userId), eq(username), eq(TotpSecret.TotpPurpose.API_KEY_CREATION), anyString()))
//                .thenReturn(expectedResponse);
//
//        // When & Then
//        mockMvc.perform(post("/api/v1/api-keys/setup-totp"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.secret").value("JBSWY3DPEHPK3PXP"))
//                .andExpect(jsonPath("$.qrCode").value("data:image/png;base64,test-qr-code"))
//                .andExpect(jsonPath("$.purpose").value("API_KEY_CREATION"))
//                .andExpect(jsonPath("$.purposeId").exists())
//                .andExpect(jsonPath("$.instructions").exists());
//
//        verify(totpUseCase).setupTotp(eq(userId), eq(username), eq(TotpSecret.TotpPurpose.API_KEY_CREATION), anyString());
//    }
//
//    @Test
//    void createApiKeyWithTotp_WithValidRequest_ShouldCreateApiKey() throws Exception {
//        // Given
//        CreateApiKeyWithTotpRequest request = new CreateApiKeyWithTotpRequest(
//                "Test Client",
//                "Test Description",
//                ApiKeyType.EXTERNAL,
//                "123456",
//                "apikey-test123-1683225600000"
//        );
//
//        ApiKey createdApiKey = ApiKey.builder()
//                .id("api-key-id")
//                .keyId(keyId)
//                .keySecret("secret123")
//                .clientName("Test Client")
//                .description("Test Description")
//                .type(ApiKeyType.EXTERNAL)
//                .active(true)
//                .createdBy(userId)
//                .createdAt(Instant.now())
//                .build();
//
//        ApiKeyResponse expectedResponse = ApiKeyResponse.builder()
//                .id("api-key-id")
//                .keyId(keyId)
//                .clientName("Test Client")
//                .description("Test Description")
//                .type(ApiKeyType.EXTERNAL)
//                .active(true)
//                .createdBy(userId)
//                .createdAt(Instant.now())
//                .build();
//
//        when(apiKeyService.generateNewWithTotp(eq(request), eq(userId)))
//                .thenReturn(createdApiKey);
//        when(apiKeyMapper.toResponse(createdApiKey)).thenReturn(expectedResponse);
//
//        // When & Then
//        mockMvc.perform(post("/api/v1/api-keys/create-with-totp")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value("api-key-id"))
//                .andExpect(jsonPath("$.keyId").value(keyId))
//                .andExpect(jsonPath("$.clientName").value("Test Client"))
//                .andExpect(jsonPath("$.description").value("Test Description"))
//                .andExpect(jsonPath("$.type").value("EXTERNAL"))
//                .andExpect(jsonPath("$.active").value(true))
//                .andExpect(jsonPath("$.createdBy").value(userId));
//
//        verify(apiKeyService).generateNewWithTotp(eq(request), eq(userId));
//        verify(apiKeyMapper).toResponse(createdApiKey);
//    }
//
//    @Test
//    void createApiKeyWithTotp_WithInvalidTotp_ShouldReturnBadRequest() throws Exception {
//        // Given
//        CreateApiKeyWithTotpRequest request = new CreateApiKeyWithTotpRequest(
//                "Test Client",
//                "Test Description",
//                ApiKeyType.EXTERNAL,
//                "invalid-code",
//                "apikey-test123-1683225600000"
//        );
//
//        when(apiKeyService.generateNewWithTotp(eq(request), eq(userId)))
//                .thenThrow(new IllegalArgumentException("Invalid TOTP code"));
//
//        // When & Then
//        mockMvc.perform(post("/api/v1/api-keys/create-with-totp")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest());
//
//        verify(apiKeyService).generateNewWithTotp(eq(request), eq(userId));
//    }
//
//    @Test
//    void getApiKeySecretWithTotp_WithValidCode_ShouldReturnSecret() throws Exception {
//        // Given
//        ApiKey apiKey = ApiKey.builder()
//                .id("api-key-id")
//                .keyId(keyId)
//                .keySecret("secret123")
//                .clientName("Test Client")
//                .description("Test Description")
//                .type(ApiKeyType.EXTERNAL)
//                .active(true)
//                .createdBy(userId)
//                .createdAt(Instant.now())
//                .build();
//
//        ApiKeyResponse expectedResponse = ApiKeyResponse.builder()
//                .id("api-key-id")
//                .keyId(keyId)
//                .keySecret("secret123")
//                .clientName("Test Client")
//                .description("Test Description")
//                .type(ApiKeyType.EXTERNAL)
//                .active(true)
//                .createdBy(userId)
//                .createdAt(Instant.now())
//                .build();
//
//        when(apiKeyService.verifyTotpForSecretViewing(eq(userId), eq("123456"), eq("purpose123")))
//                .thenReturn(true);
//        when(apiKeyUseCase.validateApiKey(eq(keyId), isNull()))
//                .thenReturn(apiKey);
//        when(apiKeyMapper.toResponse(apiKey)).thenReturn(expectedResponse);
//
//        // When & Then
//        mockMvc.perform(get("/api/v1/api-keys/{keyId}/secret", keyId)
//                        .param("totpCode", "123456")
//                        .param("purposeId", "purpose123"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.keySecret").value("secret123"))
//                .andExpect(jsonPath("$.keyId").value(keyId));
//
//        verify(apiKeyService).verifyTotpForSecretViewing(eq(userId), eq("123456"), eq("purpose123"));
//        verify(apiKeyUseCase).validateApiKey(eq(keyId), isNull());
//    }
//
//    @Test
//    void getApiKeySecretWithTotp_WithInvalidCode_ShouldReturnBadRequest() throws Exception {
//        // Given
//        when(apiKeyService.verifyTotpForSecretViewing(eq(userId), eq("invalid"), eq("purpose123")))
//                .thenReturn(false);
//
//        // When & Then
//        mockMvc.perform(get("/api/v1/api-keys/{keyId}/secret", keyId)
//                        .param("totpCode", "invalid")
//                        .param("purposeId", "purpose123"))
//                .andExpect(status().isBadRequest());
//
//        verify(apiKeyService).verifyTotpForSecretViewing(eq(userId), eq("invalid"), eq("purpose123"));
//        verify(apiKeyUseCase, never()).validateApiKey(anyString(), any());
//    }
//
//    @Test
//    void setupSecretViewingTotp_ShouldReturnTotpSetupResponse() throws Exception {
//        // Given
//        TotpSetupResponse expectedResponse = TotpSetupResponse.builder()
//                .secret("JBSWY3DPEHPK3PXP")
//                .qrCode("data:image/png;base64,test-qr-code")
//                .purposeId("secret-sk_test123-1683225600000")
//                .purpose(TotpSecret.TotpPurpose.SECRET_KEY_VIEWING)
//                .instructions("Scan QR code with authenticator app")
//                .build();
//
//        when(totpUseCase.setupTotp(eq(userId), eq(username), eq(TotpSecret.TotpPurpose.SECRET_KEY_VIEWING), anyString()))
//                .thenReturn(expectedResponse);
//
//        // When & Then
//        mockMvc.perform(post("/api/v1/api-keys/{keyId}/setup-secret-totp", keyId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.secret").value("JBSWY3DPEHPK3PXP"))
//                .andExpect(jsonPath("$.purpose").value("SECRET_KEY_VIEWING"))
//                .andExpect(jsonPath("$.purposeId").exists());
//
//        verify(totpUseCase).setupTotp(eq(userId), eq(username), eq(TotpSecret.TotpPurpose.SECRET_KEY_VIEWING), anyString());
//    }
//
//    @Test
//    void getAllApiKeys_ShouldReturnAllApiKeys() throws Exception {
//        // Given
//        ApiKey apiKey1 = ApiKey.builder().id("1").keyId("sk_1").build();
//        ApiKey apiKey2 = ApiKey.builder().id("2").keyId("sk_2").build();
//
//        ApiKeyResponse response1 = ApiKeyResponse.builder().id("1").keyId("sk_1").build();
//        ApiKeyResponse response2 = ApiKeyResponse.builder().id("2").keyId("sk_2").build();
//
//        when(apiKeyUseCase.getAllApiKeys()).thenReturn(List.of(apiKey1, apiKey2));
//        when(apiKeyMapper.toResponse(apiKey1)).thenReturn(response1);
//        when(apiKeyMapper.toResponse(apiKey2)).thenReturn(response2);
//
//        // When & Then
//        mockMvc.perform(get("/api/v1/api-keys"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$[0].id").value("1"))
//                .andExpect(jsonPath("$[1].id").value("2"));
//
//        verify(apiKeyUseCase).getAllApiKeys();
//    }
//
//    @Test
//    void getApiKeysByClient_ShouldReturnClientApiKeys() throws Exception {
//        // Given
//        String clientName = "Test Client";
//        ApiKey apiKey = ApiKey.builder().id("1").clientName(clientName).build();
//        ApiKeyResponse response = ApiKeyResponse.builder().id("1").clientName(clientName).build();
//
//        when(apiKeyUseCase.getApiKeysByClient(clientName)).thenReturn(List.of(apiKey));
//        when(apiKeyMapper.toResponse(apiKey)).thenReturn(response);
//
//        // When & Then
//        mockMvc.perform(get("/api/v1/api-keys/client/{clientName}", clientName))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$[0].clientName").value(clientName));
//
//        verify(apiKeyUseCase).getApiKeysByClient(clientName);
//    }
//
//    @Test
//    void getActiveApiKeys_ShouldReturnActiveApiKeys() throws Exception {
//        // Given
//        ApiKey apiKey = ApiKey.builder().id("1").active(true).build();
//        ApiKeyResponse response = ApiKeyResponse.builder().id("1").active(true).build();
//
//        when(apiKeyUseCase.getActiveApiKeys()).thenReturn(List.of(apiKey));
//        when(apiKeyMapper.toResponse(apiKey)).thenReturn(response);
//
//        // When & Then
//        mockMvc.perform(get("/api/v1/api-keys/active"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$[0].active").value(true));
//
//        verify(apiKeyUseCase).getActiveApiKeys();
//    }
//
//    @Test
//    void getInactiveApiKeys_ShouldReturnInactiveApiKeys() throws Exception {
//        // Given
//        ApiKey apiKey = ApiKey.builder().id("1").active(false).build();
//        ApiKeyResponse response = ApiKeyResponse.builder().id("1").active(false).build();
//
//        when(apiKeyUseCase.getInactiveApiKeys()).thenReturn(List.of(apiKey));
//        when(apiKeyMapper.toResponse(apiKey)).thenReturn(response);
//
//        // When & Then
//        mockMvc.perform(get("/api/v1/api-keys/inactive"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$[0].active").value(false));
//
//        verify(apiKeyUseCase).getInactiveApiKeys();
//    }
//
//    @Test
//    void getApiKeysByCreator_ShouldReturnCreatorApiKeys() throws Exception {
//        // Given
//        String creatorId = "creator-123";
//        ApiKey apiKey = ApiKey.builder().id("1").createdBy(creatorId).build();
//        ApiKeyResponse response = ApiKeyResponse.builder().id("1").createdBy(creatorId).build();
//
//        when(apiKeyUseCase.getApiKeysByCreator(creatorId)).thenReturn(List.of(apiKey));
//        when(apiKeyMapper.toResponse(apiKey)).thenReturn(response);
//
//        // When & Then
//        mockMvc.perform(get("/api/v1/api-keys/creator/{createdBy}", creatorId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$[0].createdBy").value(creatorId));
//
//        verify(apiKeyUseCase).getApiKeysByCreator(creatorId);
//    }
//
//    @Test
//    void activateApiKey_ShouldActivateKey() throws Exception {
//        // When & Then
//        mockMvc.perform(put("/api/v1/api-keys/{keyId}/activate", keyId))
//                .andExpect(status().isNoContent());
//
//        verify(apiKeyUseCase).activateApiKey(keyId);
//    }
//
//    @Test
//    void deactivateApiKey_ShouldDeactivateKey() throws Exception {
//        // When & Then
//        mockMvc.perform(put("/api/v1/api-keys/{keyId}/deactivate", keyId))
//                .andExpect(status().isNoContent());
//
//        verify(apiKeyUseCase).deactivateApiKey(keyId);
//    }
//
//    @Test
//    void validateApiKey_WithValidCredentials_ShouldReturnTrue() throws Exception {
//        // Given
//        when(apiKeyUseCase.isApiKeyValid(keyId, "secret123")).thenReturn(true);
//
//        // When & Then
//        mockMvc.perform(get("/api/v1/api-keys/validate")
//                        .param("keyId", keyId)
//                        .param("keySecret", "secret123"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("true"));
//
//        verify(apiKeyUseCase).isApiKeyValid(keyId, "secret123");
//    }
//
//    @Test
//    void validateApiKey_WithInvalidCredentials_ShouldReturnFalse() throws Exception {
//        // Given
//        when(apiKeyUseCase.isApiKeyValid(keyId, "wrong")).thenReturn(false);
//
//        // When & Then
//        mockMvc.perform(get("/api/v1/api-keys/validate")
//                        .param("keyId", keyId)
//                        .param("keySecret", "wrong"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("false"));
//
//        verify(apiKeyUseCase).isApiKeyValid(keyId, "wrong");
//    }
//
//    @Test
//    void getApiKeySecretWithTotp_WithNonExistentKey_ShouldThrowException() throws Exception {
//        // Given
//        when(apiKeyService.verifyTotpForSecretViewing(eq(userId), eq("123456"), eq("purpose123")))
//                .thenReturn(true);
//        when(apiKeyUseCase.validateApiKey(eq(keyId), isNull()))
//                .thenReturn(null);
//
//        // When & Then
//        mockMvc.perform(get("/api/v1/api-keys/{keyId}/secret", keyId)
//                        .param("totpCode", "123456")
//                        .param("purposeId", "purpose123"))
//                .andExpect(status().isNotFound());
//
//        verify(apiKeyService).verifyTotpForSecretViewing(eq(userId), eq("123456"), eq("purpose123"));
//        verify(apiKeyUseCase).validateApiKey(eq(keyId), isNull());
//    }
//}
