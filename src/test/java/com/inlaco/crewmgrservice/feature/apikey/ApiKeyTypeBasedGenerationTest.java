package com.inlaco.crewmgrservice.feature.apikey;

import com.inlaco.crewmgrservice.feature.apikey.application.service.ApiKeyGenerationService;
import com.inlaco.crewmgrservice.feature.apikey.infrastructure.config.ApiKeyExpirationConfig;
import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKey;
import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKeyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ApiKeyTypeBasedGenerationTest {

    @Mock
    private ApiKeyExpirationConfig expirationConfig;

    private ApiKeyGenerationService generationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        generationService = new ApiKeyGenerationService(expirationConfig);

        // Setup default expiration values
        when(expirationConfig.getExpirationForType(ApiKeyType.SHIP_SCHEDULE))
            .thenReturn(Duration.ofDays(365));
        when(expirationConfig.getExpirationForType(ApiKeyType.CREW_MANAGEMENT))
            .thenReturn(Duration.ofDays(180));
        when(expirationConfig.getExpirationForType(ApiKeyType.EXTERNAL))
            .thenReturn(Duration.ofDays(90));
        when(expirationConfig.getExpirationForType(ApiKeyType.INTERNAL))
            .thenReturn(Duration.ofDays(30));
    }

    @Test
    void shouldGenerateShipScheduleApiKey() {
        // When
        ApiKey apiKey = generationService.generateNew(
            "Test Client",
            "Ship Schedule API Key",
            "test-user",
            ApiKeyType.SHIP_SCHEDULE
        );

        // Then
        assertNotNull(apiKey);
        assertEquals("Test Client", apiKey.getClientName());
        assertEquals("Ship Schedule API Key", apiKey.getDescription());
        assertEquals("test-user", apiKey.getCreatedBy());
        assertEquals(ApiKeyType.SHIP_SCHEDULE, apiKey.getType());
        assertTrue(apiKey.isActive());
        assertNotNull(apiKey.getCreatedAt());

        // Verify expiration is 1 year from now
        Instant expectedExpiration = apiKey.getCreatedAt().plus(Duration.ofDays(365));
        assertEquals(expectedExpiration, apiKey.getExpiresAt());
        assertTrue(apiKey.isValid());
    }

    @Test
    void shouldGenerateCrewManagementApiKey() {
        // When
        ApiKey apiKey = generationService.generateNew(
            "Test Client",
            "Crew Management API Key",
            "test-user",
            ApiKeyType.CREW_MANAGEMENT
        );

        // Then
        assertNotNull(apiKey);
        assertEquals("Test Client", apiKey.getClientName());
        assertEquals("Crew Management API Key", apiKey.getDescription());
        assertEquals("test-user", apiKey.getCreatedBy());
        assertEquals(ApiKeyType.CREW_MANAGEMENT, apiKey.getType());
        assertTrue(apiKey.isActive());
        assertNotNull(apiKey.getCreatedAt());

        // Verify expiration is 180 days from now
        Instant expectedExpiration = apiKey.getCreatedAt().plus(Duration.ofDays(180));
        assertEquals(expectedExpiration, apiKey.getExpiresAt());
        assertTrue(apiKey.isValid());
    }

    @Test
    void shouldGenerateExternalApiKey() {
        // When
        ApiKey apiKey = generationService.generateNew(
            "Test Client",
            "External API Key",
            "test-user",
            ApiKeyType.EXTERNAL
        );

        // Then
        assertNotNull(apiKey);
        assertEquals("Test Client", apiKey.getClientName());
        assertEquals("External API Key", apiKey.getDescription());
        assertEquals("test-user", apiKey.getCreatedBy());
        assertEquals(ApiKeyType.EXTERNAL, apiKey.getType());
        assertTrue(apiKey.isActive());
        assertNotNull(apiKey.getCreatedAt());

        // Verify expiration is 90 days from now
        Instant expectedExpiration = apiKey.getCreatedAt().plus(Duration.ofDays(90));
        assertEquals(expectedExpiration, apiKey.getExpiresAt());
        assertTrue(apiKey.isValid());
    }

    @Test
    void shouldGenerateInternalApiKey() {
        // When
        ApiKey apiKey = generationService.generateNew(
            "Test Client",
            "Internal API Key",
            "test-user",
            ApiKeyType.INTERNAL
        );

        // Then
        assertNotNull(apiKey);
        assertEquals("Test Client", apiKey.getClientName());
        assertEquals("Internal API Key", apiKey.getDescription());
        assertEquals("test-user", apiKey.getCreatedBy());
        assertEquals(ApiKeyType.INTERNAL, apiKey.getType());
        assertTrue(apiKey.isActive());
        assertNotNull(apiKey.getCreatedAt());

        // Verify expiration is 30 days from now
        Instant expectedExpiration = apiKey.getCreatedAt().plus(Duration.ofDays(30));
        assertEquals(expectedExpiration, apiKey.getExpiresAt());
        assertTrue(apiKey.isValid());
    }

    @Test
    void shouldGenerateDefaultExternalApiKeyWhenTypeNotSpecified() {
        // When
        ApiKey apiKey = generationService.generateNew(
            "Test Client",
            "Default API Key",
            "test-user"
        );

        // Then
        assertNotNull(apiKey);
        assertEquals("Test Client", apiKey.getClientName());
        assertEquals("Default API Key", apiKey.getDescription());
        assertEquals("test-user", apiKey.getCreatedBy());
        assertEquals(ApiKeyType.EXTERNAL, apiKey.getType()); // Should default to EXTERNAL
        assertTrue(apiKey.isActive());
        assertNotNull(apiKey.getCreatedAt());

        // Verify expiration is 90 days (default for EXTERNAL)
        Instant expectedExpiration = apiKey.getCreatedAt().plus(Duration.ofDays(90));
        assertEquals(expectedExpiration, apiKey.getExpiresAt());
        assertTrue(apiKey.isValid());
    }

    @Test
    void shouldVerifyApiKeyTypeEnum() {
        // Test enum values
        assertEquals("SHIP_SCHEDULE", ApiKeyType.SHIP_SCHEDULE.getCode());
        assertEquals("CREW_MANAGEMENT", ApiKeyType.CREW_MANAGEMENT.getCode());
        assertEquals("EXTERNAL", ApiKeyType.EXTERNAL.getCode());
        assertEquals("INTERNAL", ApiKeyType.INTERNAL.getCode());

        assertEquals("Ship Schedule API Key", ApiKeyType.SHIP_SCHEDULE.getDescription());
        assertEquals("Crew Management API Key", ApiKeyType.CREW_MANAGEMENT.getDescription());
        assertEquals("External API Key", ApiKeyType.EXTERNAL.getDescription());
        assertEquals("Internal API Key", ApiKeyType.INTERNAL.getDescription());
    }

    @Test
    void shouldVerifyExpirationConfigReturnsAllTypes() {
        // When
        Map<ApiKeyType, Duration> allExpirations = expirationConfig.getAllExpirationTimes();

        // Then
        assertEquals(4, allExpirations.size());
        assertTrue(allExpirations.containsKey(ApiKeyType.SHIP_SCHEDULE));
        assertTrue(allExpirations.containsKey(ApiKeyType.CREW_MANAGEMENT));
        assertTrue(allExpirations.containsKey(ApiKeyType.EXTERNAL));
        assertTrue(allExpirations.containsKey(ApiKeyType.INTERNAL));
    }
}
