//package com.inlaco.crewmgrservice.feature.shipschedule;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKey;
//import com.inlaco.crewmgrservice.feature.apikey.application.port.in.ApiKeyUseCase;
//import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.ShipScheduleUseCase;
//import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewUseCase;
//import com.inlaco.crewmgrservice.feature.crew.presentation.dto.response.CrewProfileResponse;
//import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
//import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.ScheduleStatus;
//import com.inlaco.crewmgrservice.feature.shipschedule.presentation.rest.controller.ExternalApiController;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.Instant;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ExternalApiController.class)
//class ExternalApiControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ShipScheduleUseCase shipScheduleUseCase;
//
//    @MockBean
//    private CrewUseCase crewUseCase;
//
//    @MockBean
//    private ApiKeyUseCase apiKeyUseCase;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private ShipSchedule testSchedule;
//    private ApiKey testApiKey;
//
//    @BeforeEach
//    void setUp() {
//        testSchedule = ShipSchedule.builder()
//                .id("schedule-123")
//                .clientId("client-456")
//                .shipId("ship-789")
//                .shipName("Test Ship")
//                .route("Test Route")
//                .departureTime(Instant.now().plusSeconds(3600))
//                .arrivalTime(Instant.now().plusSeconds(7200))
//                .departurePort("Port A")
//                .arrivalPort("Port B")
//                .status(ScheduleStatus.CONFIRMED)
//                .employeeCardIds(List.of("emp-001", "emp-002"))
//                .createdAt(Instant.now())
//                .updatedAt(Instant.now())
//                .build();
//
//        testApiKey = ApiKey.builder()
//                .id("key-123")
//                .keyId("sk_test123")
//                .keySecret("secret456")
//                .clientName("Test Client")
//                .active(true)
//                .createdAt(Instant.now())
//                .expiresAt(Instant.now().plusSeconds(86400))
//                .build();
//    }
//
//    @Test
//    @WithMockUser(username = "Test Client")
//    void shouldGetAllSchedulesBasic() throws Exception {
//        // Given
//        when(shipScheduleUseCase.getAllSchedules()).thenReturn(List.of(testSchedule));
//
//        // When & Then
//        mockMvc.perform(get("/api/v1/external/ship-schedules")
//                        .header("X-API-Key-ID", "sk_test123")
//                        .header("X-API-Key-Secret", "secret456")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$[0].id").value("schedule-123"))
//                .andExpect(jsonPath("$[0].clientId").value("client-456"))
//                .andExpect(jsonPath("$[0].shipName").value("Test Ship"))
//                .andExpect(jsonPath("$[0].status").value("CONFIRMED"));
//    }
//
//    @Test
//    @WithMockUser(username = "Test Client")
//    void shouldGetScheduleCrewInfo() throws Exception {
//        // Given
//        when(shipScheduleUseCase.getScheduleById("schedule-123")).thenReturn(Optional.of(testSchedule));
//        when(crewUseCase.getProfilesByEmployeeCardIds(anyList())).thenReturn(List.of());
//
//        // When & Then
//        mockMvc.perform(get("/api/v1/external/ship-schedules/schedule-123/crew")
//                        .header("X-API-Key-ID", "sk_test123")
//                        .header("X-API-Key-Secret", "secret456")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value("schedule-123"))
//                .andExpect(jsonPath("$.clientId").value("client-456"))
//                .andExpect(jsonPath("$.shipName").value("Test Ship"))
//                .andExpect(jsonPath("$.status").value("CONFIRMED"))
//                .andExpect(jsonPath("$.crewMembers").isArray());
//    }
//
//    @Test
//    @WithMockUser(username = "Test Client")
//    void shouldReturn404WhenScheduleNotFound() throws Exception {
//        // Given
//        when(shipScheduleUseCase.getScheduleById("nonexistent")).thenReturn(Optional.empty());
//
//        // When & Then
//        mockMvc.perform(get("/api/v1/external/ship-schedules/nonexistent/crew")
//                        .header("X-API-Key-ID", "sk_test123")
//                        .header("X-API-Key-Secret", "secret456")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void shouldReturn401WithoutApiKey() throws Exception {
//        // When & Then
//        mockMvc.perform(get("/api/v1/external/ship-schedules")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    @WithMockUser(username = "Test Client")
//    void shouldGetHealthCheck() throws Exception {
//        // When & Then
//        mockMvc.perform(get("/api/v1/external/health")
//                        .header("X-API-Key-ID", "sk_test123")
//                        .header("X-API-Key-Secret", "secret456")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string("API is accessible. Authenticated as: Test Client"));
//    }
//}
