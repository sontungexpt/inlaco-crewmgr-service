package com.inlaco.crewmgrservice.feature.shipschedule.mapper;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.ScheduleStatus;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.ShipScheduleEntity;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.mapper.ShipScheduleEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipScheduleEntityMapperTest {

    private ShipScheduleEntityMapper mapper;
    private ShipSchedule testDomain;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ShipScheduleEntityMapper.class);
        
        testDomain = ShipSchedule.builder()
                .id("test-id-123")
                .clientId("client-456")
                .shipId("ship-789")
                .shipName("Test Ship")
                .route("Test Route")
                .departureTime(Instant.now().plusSeconds(3600))
                .arrivalTime(Instant.now().plusSeconds(7200))
                .departurePort("Port A")
                .arrivalPort("Port B")
                .status(ScheduleStatus.CONFIRMED)
                .employeeCardIds(List.of("emp-001", "emp-002"))
                .createdBy("test-user")
                .createdAt(Instant.now())
                .updatedBy("test-user")
                .updatedAt(Instant.now())
                .build();
    }

    @Test
    void shouldMapDomainToEntityIgnoringId() {
        // When
        ShipScheduleEntity entity = mapper.toEntity(testDomain);

        // Then
        assertNull(entity.getId(), "ID should be null for MongoDB auto-generation");
        assertEquals(testDomain.getClientId(), entity.getClientId());
        assertEquals(testDomain.getShipId(), entity.getShipId());
        assertEquals(testDomain.getShipName(), entity.getShipName());
        assertEquals(testDomain.getRoute(), entity.getRoute());
        assertEquals(testDomain.getDepartureTime(), entity.getDepartureTime());
        assertEquals(testDomain.getArrivalTime(), entity.getArrivalTime());
        assertEquals(testDomain.getDeparturePort(), entity.getDeparturePort());
        assertEquals(testDomain.getArrivalPort(), entity.getArrivalPort());
        assertEquals(testDomain.getStatus(), entity.getStatus());
        assertEquals(testDomain.getEmployeeCardIds(), entity.getEmployeeCardIds());
        assertEquals(testDomain.getCreatedBy(), entity.getCreatedBy());
        assertEquals(testDomain.getCreatedAt(), entity.getCreatedAt());
        assertEquals(testDomain.getUpdatedBy(), entity.getUpdatedBy());
        assertEquals(testDomain.getUpdatedAt(), entity.getUpdatedAt());
    }

    @Test
    void shouldMapEntityToDomainIncludingId() {
        // Given
        ShipScheduleEntity entity = ShipScheduleEntity.builder()
                .id("generated-id-456")
                .clientId("client-456")
                .shipId("ship-789")
                .shipName("Test Ship")
                .route("Test Route")
                .departureTime(Instant.now().plusSeconds(3600))
                .arrivalTime(Instant.now().plusSeconds(7200))
                .departurePort("Port A")
                .arrivalPort("Port B")
                .status(ScheduleStatus.CONFIRMED)
                .employeeCardIds(List.of("emp-001", "emp-002"))
                .createdBy("test-user")
                .createdAt(Instant.now())
                .updatedBy("test-user")
                .updatedAt(Instant.now())
                .build();

        // When
        ShipSchedule domain = mapper.toDomain(entity);

        // Then
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getClientId(), domain.getClientId());
        assertEquals(entity.getShipId(), domain.getShipId());
        assertEquals(entity.getShipName(), domain.getShipName());
        assertEquals(entity.getRoute(), domain.getRoute());
        assertEquals(entity.getDepartureTime(), domain.getDepartureTime());
        assertEquals(entity.getArrivalTime(), domain.getArrivalTime());
        assertEquals(entity.getDeparturePort(), domain.getDeparturePort());
        assertEquals(entity.getArrivalPort(), domain.getArrivalPort());
        assertEquals(entity.getStatus(), domain.getStatus());
        assertEquals(entity.getEmployeeCardIds(), domain.getEmployeeCardIds());
        assertEquals(entity.getCreatedBy(), domain.getCreatedBy());
        assertEquals(entity.getCreatedAt(), domain.getCreatedAt());
        assertEquals(entity.getUpdatedBy(), domain.getUpdatedBy());
        assertEquals(entity.getUpdatedAt(), domain.getUpdatedAt());
    }

    @Test
    void shouldMapEntityToDomainWithoutId() {
        // Given
        ShipScheduleEntity entity = ShipScheduleEntity.builder()
                .id("generated-id-456")
                .clientId("client-456")
                .shipId("ship-789")
                .shipName("Test Ship")
                .route("Test Route")
                .departureTime(Instant.now().plusSeconds(3600))
                .arrivalTime(Instant.now().plusSeconds(7200))
                .departurePort("Port A")
                .arrivalPort("Port B")
                .status(ScheduleStatus.CONFIRMED)
                .employeeCardIds(List.of("emp-001", "emp-002"))
                .createdBy("test-user")
                .createdAt(Instant.now())
                .updatedBy("test-user")
                .updatedAt(Instant.now())
                .build();

        // When
        ShipSchedule domain = mapper.toDomainWithoutId(entity);

        // Then
        assertNull(domain.getId(), "ID should be ignored");
        assertEquals(entity.getClientId(), domain.getClientId());
        assertEquals(entity.getShipId(), domain.getShipId());
        assertEquals(entity.getShipName(), domain.getShipName());
        assertEquals(entity.getRoute(), domain.getRoute());
        assertEquals(entity.getDepartureTime(), domain.getDepartureTime());
        assertEquals(entity.getArrivalTime(), domain.getArrivalTime());
        assertEquals(entity.getDeparturePort(), domain.getDeparturePort());
        assertEquals(entity.getArrivalPort(), domain.getArrivalPort());
        assertEquals(entity.getStatus(), domain.getStatus());
        assertEquals(entity.getEmployeeCardIds(), domain.getEmployeeCardIds());
        assertEquals(entity.getCreatedBy(), domain.getCreatedBy());
        assertEquals(entity.getCreatedAt(), domain.getCreatedAt());
        assertEquals(entity.getUpdatedBy(), domain.getUpdatedBy());
        assertEquals(entity.getUpdatedAt(), domain.getUpdatedAt());
    }

    @Test
    void shouldUpdateEntityIgnoringId() {
        // Given
        ShipScheduleEntity existingEntity = ShipScheduleEntity.builder()
                .id("existing-id-123")
                .clientId("old-client")
                .shipId("old-ship")
                .shipName("Old Ship")
                .route("Old Route")
                .departureTime(Instant.now().plusSeconds(3600))
                .arrivalTime(Instant.now().plusSeconds(7200))
                .departurePort("Old Port A")
                .arrivalPort("Old Port B")
                .status(ScheduleStatus.DRAFT)
                .employeeCardIds(List.of("old-emp"))
                .createdBy("old-user")
                .createdAt(Instant.now().minusSeconds(3600))
                .updatedBy("old-user")
                .updatedAt(Instant.now().minusSeconds(1800))
                .build();

        ShipSchedule updateData = ShipSchedule.builder()
                .clientId("new-client")
                .shipId("new-ship")
                .shipName("New Ship")
                .route("New Route")
                .departureTime(Instant.now().plusSeconds(7200))
                .arrivalTime(Instant.now().plusSeconds(10800))
                .departurePort("New Port A")
                .arrivalPort("New Port B")
                .status(ScheduleStatus.CONFIRMED)
                .employeeCardIds(List.of("new-emp-1", "new-emp-2"))
                .createdBy("new-creator")
                .createdAt(Instant.now())
                .updatedBy("new-updater")
                .updatedAt(Instant.now())
                .build();

        // When
        mapper.updateEntity(existingEntity, updateData);

        // Then
        assertEquals("existing-id-123", existingEntity.getId(), "ID should not be updated");
        assertEquals("new-client", existingEntity.getClientId());
        assertEquals("new-ship", existingEntity.getShipId());
        assertEquals("New Ship", existingEntity.getShipName());
        assertEquals("New Route", existingEntity.getRoute());
        assertEquals("new-creator", existingEntity.getCreatedBy());
        assertEquals("new-updater", existingEntity.getUpdatedBy());
        assertEquals(ScheduleStatus.CONFIRMED, existingEntity.getStatus());
        assertEquals(List.of("new-emp-1", "new-emp-2"), existingEntity.getEmployeeCardIds());
    }
}
