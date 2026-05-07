//package com.inlaco.crewmgrservice.feature.apikey;
//
//import com.inlaco.crewmgrservice.feature.apikey.application.service.ApiKeyService;
//import com.inlaco.crewmgrservice.feature.apikey.infrastructure.config.ApiKeyConfig;
//import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKey;
//import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKeyType;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.Duration;
//import java.time.Instant;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//class ApiKeyTypeBasedGenerationTest {
//
//    @Mock
//    private ApiKeyConfig apiKeyConfig;
//
//    private ApiKeyService apiKeyService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        apiKeyService = new ApiKeyService(null, apiKeyConfig, null);
//
//        // Setup default expiration values
//        ApiKeyConfig.ExpirationConfig shipScheduleConfig = new ApiKeyConfig.ExpirationConfig(Duration.ofDays(365), true);
//        ApiKeyConfig.ExpirationConfig crewManagementConfig = new ApiKeyConfig.ExpirationConfig(Duration.ofDays(180), true);
//        ApiKeyConfig.ExpirationConfig externalConfig = new ApiKeyConfig.ExpirationConfig(Duration.ofDays(90), true);
//        ApiKeyConfig.ExpirationConfig internalConfig = new ApiKeyConfig.ExpirationConfig(Duration.ofDays(30), true);
//
//        Map<ApiKeyType, ApiKeyConfig.ExpirationConfig> byType = Map.of(
//            ApiKeyType.SHIP_SCHEDULE, shipScheduleConfig,
//            ApiKeyType.CREW_MANAGEMENT, crewManagementConfig,
//            ApiKeyType.EXTERNAL, externalConfig,
//            ApiKeyType.INTERNAL, internalConfig
//        );
//
//        ApiKeyConfig.Expiration expiration = new ApiKeyConfig.Expiration(Duration.ofDays(90), true, byType);
//        when(apiKeyConfig.expiration()).thenReturn(expiration);
//    }
//
//    @Test
//    void shouldGenerateShipScheduleApiKey() {
//        // When
//        ApiKey apiKey = apiKeyService.generateNew(
//            "Test Client",
//            "Ship Schedule API Key",
//            "test-user",
//            ApiKeyType.SHIP_SCHEDULE
//        );
//
//        // Then
//        assertNotNull(apiKey);
//        assertEquals("Test Client", apiKey.getClientName());
//        assertEquals("Ship Schedule API Key", apiKey.getDescription());
//        assertEquals("test-user", apiKey.getCreatedBy());
//        assertEquals(ApiKeyType.SHIP_SCHEDULE, apiKey.getType());
//        assertTrue(apiKey.isActive());
//        assertNotNull(apiKey.getCreatedAt());
//
//        // Verify expiration is 1 year from now
//        Instant expectedExpiration = apiKey.getCreatedAt().plus(Duration.ofDays(365));
//        assertEquals(expectedExpiration, apiKey.getExpiresAt());
//        assertTrue(apiKey.isValid());
//    }
//
//    @Test
//    void shouldGenerateCrewManagementApiKey() {
//        // When
//        ApiKey apiKey = apiKeyService.generateNew(
//            "Test Client",
//            "Crew Management API Key",
//            "test-user",
//            ApiKeyType.CREW_MANAGEMENT
//        );
//
//        // Then
//        assertNotNull(apiKey);
//        assertEquals("Test Client", apiKey.getClientName());
//        assertEquals("Crew Management API Key", apiKey.getDescription());
//        assertEquals("test-user", apiKey.getCreatedBy());
//        assertEquals(ApiKeyType.CREW_MANAGEMENT, apiKey.getType());
//        assertTrue(apiKey.isActive());
//        assertNotNull(apiKey.getCreatedAt());
//
//        // Verify expiration is 180 days from now
//        Instant expectedExpiration = apiKey.getCreatedAt().plus(Duration.ofDays(180));
//        assertEquals(expectedExpiration, apiKey.getExpiresAt());
//        assertTrue(apiKey.isValid());
//    }
//
//    @Test
//    void shouldGenerateExternalApiKey() {
//        // When
//        ApiKey apiKey = apiKeyService.generateNew(
//            "Test Client",
//            "External API Key",
//            "test-user",
//            ApiKeyType.EXTERNAL
//        );
//
//        // Then
//        assertNotNull(apiKey);
//        assertEquals("Test Client", apiKey.getClientName());
//        assertEquals("External API Key", apiKey.getDescription());
//        assertEquals("test-user", apiKey.getCreatedBy());
//        assertEquals(ApiKeyType.EXTERNAL, apiKey.getType());
//        assertTrue(apiKey.isActive());
//        assertNotNull(apiKey.getCreatedAt());
//
//        // Verify expiration is 90 days from now
//        Instant expectedExpiration = apiKey.getCreatedAt().plus(Duration.ofDays(90));
//        assertEquals(expectedExpiration, apiKey.getExpiresAt());
//        assertTrue(apiKey.isValid());
//    }
//
//    @Test
//    void shouldGenerateInternalApiKey() {
//        // When
//        ApiKey apiKey = apiKeyService.generateNew(
//            "Test Client",
//            "Internal API Key",
//            "test-user",
//            ApiKeyType.INTERNAL
//        );
//
//        // Then
//        assertNotNull(apiKey);
//        assertEquals("Test Client", apiKey.getClientName());
//        assertEquals("Internal API Key", apiKey.getDescription());
//        assertEquals("test-user", apiKey.getCreatedBy());
//        assertEquals(ApiKeyType.INTERNAL, apiKey.getType());
//        assertTrue(apiKey.isActive());
//        assertNotNull(apiKey.getCreatedAt());
//
//        // Verify expiration is 30 days from now
//        Instant expectedExpiration = apiKey.getCreatedAt().plus(Duration.ofDays(30));
//        assertEquals(expectedExpiration, apiKey.getExpiresAt());
//        assertTrue(apiKey.isValid());
//    }
//
//    @Test
//    void shouldGenerateDefaultExternalApiKeyWhenTypeNotSpecified() {
//        // When
//        ApiKey apiKey = apiKeyService.generateNew(
//            "Test Client",
//            "Default API Key",
//            "test-user"
//        );
//
//        // Then
//        assertNotNull(apiKey);
//        assertEquals("Test Client", apiKey.getClientName());
//        assertEquals("Default API Key", apiKey.getDescription());
//        assertEquals("test-user", apiKey.getCreatedBy());
//        assertEquals(ApiKeyType.EXTERNAL, apiKey.getType()); // Should default to EXTERNAL
//        assertTrue(apiKey.isActive());
//        assertNotNull(apiKey.getCreatedAt());
//
//        // Verify expiration is 90 days (default for EXTERNAL)
//        Instant expectedExpiration = apiKey.getCreatedAt().plus(Duration.ofDays(90));
//        assertEquals(expectedExpiration, apiKey.getExpiresAt());
//        assertTrue(apiKey.isValid());
//    }
//
//    @Test
//    void shouldVerifyApiKeyTypeEnum() {
//        // Test enum values
//        assertEquals("SHIP_SCHEDULE", ApiKeyType.SHIP_SCHEDULE.getCode());
//        assertEquals("CREW_MANAGEMENT", ApiKeyType.CREW_MANAGEMENT.getCode());
//        assertEquals("EXTERNAL", ApiKeyType.EXTERNAL.getCode());
//        assertEquals("INTERNAL", ApiKeyType.INTERNAL.getCode());
//
//        assertEquals("Ship Schedule API Key", ApiKeyType.SHIP_SCHEDULE.getDescription());
//        assertEquals("Crew Management API Key", ApiKeyType.CREW_MANAGEMENT.getDescription());
//        assertEquals("External API Key", ApiKeyType.EXTERNAL.getDescription());
//        assertEquals("Internal API Key", ApiKeyType.INTERNAL.getDescription());
//    }
//
//    @Test
//    void shouldVerifyExpirationConfigReturnsAllTypes() {
//        // When
//        Map<ApiKeyType, ApiKeyConfig.ExpirationConfig> allExpirations = apiKeyConfig.expiration().byType();
//
//        // Then
//        assertEquals(4, allExpirations.size());
//        assertTrue(allExpirations.containsKey(ApiKeyType.SHIP_SCHEDULE));
//        assertTrue(allExpirations.containsKey(ApiKeyType.CREW_MANAGEMENT));
//        assertTrue(allExpirations.containsKey(ApiKeyType.EXTERNAL));
//        assertTrue(allExpirations.containsKey(ApiKeyType.INTERNAL));
//    }
//}
