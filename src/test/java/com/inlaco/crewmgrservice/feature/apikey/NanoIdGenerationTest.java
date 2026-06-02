//package com.inlaco.crewmgrservice.feature.apikey;
//
//import com.aventrix.jnanoid.NanoId;
//import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKey;
//import org.junit.jupiter.api.Test;
//
//import java.time.Instant;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class NanoIdGenerationTest {
//
//    @Test
//    void shouldGenerateApiKeyWithNanoId() {
//        // When
//        ApiKey apiKey = ApiKey.generateNew("Test Client", "Test Description", "test-user");
//
//        // Then
//        assertNotNull(apiKey);
//        assertNotNull(apiKey.getKeyId());
//        assertNotNull(apiKey.getKeySecret());
//        assertEquals("Test Client", apiKey.getClientName());
//        assertEquals("Test Description", apiKey.getDescription());
//        assertTrue(apiKey.isActive());
//        assertNotNull(apiKey.getCreatedAt());
//        assertNotNull(apiKey.getExpiresAt());
//        assertEquals("test-user", apiKey.getCreatedBy());
//        assertTrue(apiKey.isValid());
//    }
//
//    @Test
//    void shouldGenerateKeyIdWithCorrectFormat() {
//        // When
//        ApiKey apiKey = ApiKey.generateNew("Test Client", "Test Description", "test-user");
//
//        // Then
//        assertTrue(apiKey.getKeyId().startsWith("sk_"), "KeyId should start with 'sk_'");
//        assertTrue(apiKey.getKeyId().length() > 3, "KeyId should be longer than prefix");
//        assertFalse(apiKey.getKeyId().contains("-"), "KeyId should not contain dashes");
//    }
//
//    @Test
//    void shouldGenerateKeySecretWithCorrectLength() {
//        // When
//        ApiKey apiKey = ApiKey.generateNew("Test Client", "Test Description", "test-user");
//
//        // Then
//        assertEquals(42, apiKey.getKeySecret().length(), "KeySecret should be 42 characters");
//        assertFalse(apiKey.getKeySecret().contains("-"), "KeySecret should not contain dashes");
//    }
//
//    @Test
//    void shouldGenerateUniqueIds() {
//        // When
//        ApiKey apiKey1 = ApiKey.generateNew("Test Client", "Test Description", "test-user");
//        ApiKey apiKey2 = ApiKey.generateNew("Test Client", "Test Description", "test-user");
//
//        // Then
//        assertNotEquals(apiKey1.getKeyId(), apiKey2.getKeyId(), "KeyIds should be unique");
//        assertNotEquals(apiKey1.getKeySecret(), apiKey2.getKeySecret(), "KeySecrets should be unique");
//    }
//
//    @Test
//    void shouldSetCorrectExpiration() {
//        // When
//        Instant beforeCreation = Instant.now();
//        ApiKey apiKey = ApiKey.generateNew("Test Client", "Test Description", "test-user");
//        Instant afterCreation = Instant.now();
//
//        // Then
//        assertTrue(apiKey.getCreatedAt().isAfter(beforeCreation) || apiKey.getCreatedAt().equals(beforeCreation));
//        assertTrue(apiKey.getCreatedAt().isBefore(afterCreation) || apiKey.getCreatedAt().equals(afterCreation));
//
//        Instant expectedExpiration = apiKey.getCreatedAt().plusSeconds(365 * 24 * 60 * 60); // 1 year
//        assertTrue(apiKey.getExpiresAt().isAfter(expectedExpiration.minusSeconds(1)) ||
//                  apiKey.getExpiresAt().equals(expectedExpiration.minusSeconds(1)));
//        assertTrue(apiKey.getExpiresAt().isBefore(expectedExpiration.plusSeconds(1)) ||
//                  apiKey.getExpiresAt().equals(expectedExpiration.plusSeconds(1)));
//    }
//
//    @Test
//    void shouldValidateNanoIdGeneration() {
//        // Test direct NanoId generation
//        String nanoId1 = NanoId.randomNanoId();
//        String nanoId2 = NanoId.randomNanoId();
//        String nanoId3 = NanoId.randomNanoId(21);
//        String nanoId4 = NanoId.randomNanoId(42);
//
//        // Then
//        assertNotNull(nanoId1);
//        assertNotNull(nanoId2);
//        assertNotNull(nanoId3);
//        assertNotNull(nanoId4);
//
//        assertNotEquals(nanoId1, nanoId2, "NanoIds should be unique");
//        assertEquals(21, nanoId3.length(), "Custom length NanoId should be 21 characters");
//        assertEquals(42, nanoId4.length(), "Custom length NanoId should be 42 characters");
//
//        // Verify no dashes (unlike UUID)
//        assertFalse(nanoId1.contains("-"), "NanoId should not contain dashes");
//        assertFalse(nanoId2.contains("-"), "NanoId should not contain dashes");
//        assertFalse(nanoId3.contains("-"), "NanoId should not contain dashes");
//        assertFalse(nanoId4.contains("-"), "NanoId should not contain dashes");
//    }
//}
