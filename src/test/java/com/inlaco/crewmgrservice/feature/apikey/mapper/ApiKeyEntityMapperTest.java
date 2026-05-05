package com.inlaco.crewmgrservice.feature.apikey.mapper;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKey;
import com.inlaco.crewmgrservice.feature.apikey.infrastructure.persistence.mongodb.entity.ApiKeyEntity;
import com.inlaco.crewmgrservice.feature.apikey.infrastructure.persistence.mongodb.mapper.ApiKeyEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ApiKeyEntityMapperTest {

    private ApiKeyEntityMapper mapper;
    private ApiKey testDomain;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ApiKeyEntityMapper.class);
        
        testDomain = ApiKey.builder()
                .id("test-id-123")
                .keyId("sk_" + NanoIdUtils.randomNanoId())
                .keySecret(NanoIdUtils.randomNanoId() + NanoIdUtils.randomNanoId())
                .clientName("Test Client")
                .description("Test API Key")
                .active(true)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(86400))
                .createdBy("test-user")
                .build();
    }

    @Test
    void shouldMapDomainToEntityIgnoringId() {
        // When
        ApiKeyEntity entity = mapper.toEntity(testDomain);

        // Then
        assertNull(entity.getId(), "ID should be null for MongoDB auto-generation");
        assertEquals(testDomain.getKeyId(), entity.getKeyId());
        assertEquals(testDomain.getKeySecret(), entity.getKeySecret());
        assertEquals(testDomain.getClientName(), entity.getClientName());
        assertEquals(testDomain.getDescription(), entity.getDescription());
        assertEquals(testDomain.isActive(), entity.isActive());
        assertEquals(testDomain.getCreatedAt(), entity.getCreatedAt());
        assertEquals(testDomain.getExpiresAt(), entity.getExpiresAt());
        assertEquals(testDomain.getCreatedBy(), entity.getCreatedBy());
    }

    @Test
    void shouldMapEntityToDomainIncludingId() {
        // Given
        ApiKeyEntity entity = ApiKeyEntity.builder()
                .id("generated-id-456")
                .keyId("sk_" + NanoIdUtils.randomNanoId())
                .keySecret(NanoIdUtils.randomNanoId() + NanoIdUtils.randomNanoId())
                .clientName("Test Client")
                .description("Test API Key")
                .active(true)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(86400))
                .createdBy("test-user")
                .build();

        // When
        ApiKey domain = mapper.toDomain(entity);

        // Then
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getKeyId(), domain.getKeyId());
        assertEquals(entity.getKeySecret(), domain.getKeySecret());
        assertEquals(entity.getClientName(), domain.getClientName());
        assertEquals(entity.getDescription(), domain.getDescription());
        assertEquals(entity.isActive(), domain.isActive());
        assertEquals(entity.getCreatedAt(), domain.getCreatedAt());
        assertEquals(entity.getExpiresAt(), domain.getExpiresAt());
        assertEquals(entity.getCreatedBy(), domain.getCreatedBy());
    }

    @Test
    void shouldMapEntityToDomainWithoutId() {
        // Given
        ApiKeyEntity entity = ApiKeyEntity.builder()
                .id("generated-id-456")
                .keyId("sk_" + NanoIdUtils.randomNanoId())
                .keySecret(NanoIdUtils.randomNanoId() + NanoIdUtils.randomNanoId())
                .clientName("Test Client")
                .description("Test API Key")
                .active(true)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(86400))
                .createdBy("test-user")
                .build();

        // When
        ApiKey domain = mapper.toDomainWithoutId(entity);

        // Then
        assertNull(domain.getId(), "ID should be ignored");
        assertEquals(entity.getKeyId(), domain.getKeyId());
        assertEquals(entity.getKeySecret(), domain.getKeySecret());
        assertEquals(entity.getClientName(), domain.getClientName());
        assertEquals(entity.getDescription(), domain.getDescription());
        assertEquals(entity.isActive(), domain.isActive());
        assertEquals(entity.getCreatedAt(), domain.getCreatedAt());
        assertEquals(entity.getExpiresAt(), domain.getExpiresAt());
        assertEquals(entity.getCreatedBy(), domain.getCreatedBy());
    }

    @Test
    void shouldUpdateEntityIgnoringId() {
        // Given
        ApiKeyEntity existingEntity = ApiKeyEntity.builder()
                .id("existing-id-123")
                .keyId("sk_old")
                .keySecret("old-secret")
                .clientName("Old Client")
                .description("Old Description")
                .active(false)
                .createdAt(Instant.now().minusSeconds(3600))
                .expiresAt(Instant.now().minusSeconds(1800))
                .createdBy("old-user")
                .build();

        ApiKey updateData = ApiKey.builder()
                .keyId("sk_" + NanoIdUtils.randomNanoId())
                .keySecret(NanoIdUtils.randomNanoId() + NanoIdUtils.randomNanoId())
                .clientName("New Client")
                .description("New Description")
                .active(true)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(86400))
                .createdBy("new-user")
                .build();

        // When
        mapper.updateEntity(existingEntity, updateData);

        // Then
        assertEquals("existing-id-123", existingEntity.getId(), "ID should not be updated");
        assertEquals("sk_new", existingEntity.getKeyId());
        assertEquals("new-secret", existingEntity.getKeySecret());
        assertEquals("New Client", existingEntity.getClientName());
        assertEquals("New Description", existingEntity.getDescription());
        assertTrue(existingEntity.isActive());
        assertEquals("new-user", existingEntity.getCreatedBy());
    }

    @Test
    void shouldHandleNullValues() {
        // Given
        ApiKey domainWithNulls = ApiKey.builder()
                .id("test-id")
                .keyId("sk_" + NanoIdUtils.randomNanoId())
                .keySecret(NanoIdUtils.randomNanoId())
                .clientName("Test Client")
                .description(null)
                .active(true)
                .createdAt(Instant.now())
                .expiresAt(null)
                .createdBy("test-user")
                .build();

        // When
        ApiKeyEntity entity = mapper.toEntity(domainWithNulls);

        // Then
        assertNull(entity.getId(), "ID should be null for MongoDB auto-generation");
        assertNull(entity.getDescription(), "Description should be null");
        assertNull(entity.getExpiresAt(), "ExpiresAt should be null");
        assertTrue(entity.getKeyId().startsWith("sk_"), "KeyId should start with 'sk_'");
        assertTrue(entity.getKeySecret().length() > 0, "KeySecret should have positive length");
        assertEquals("Test Client", entity.getClientName());
        assertTrue(entity.isActive());
        assertEquals("test-user", entity.getCreatedBy());
    }
}
