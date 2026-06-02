//package com.inlaco.crewmgrservice.integration;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKey;
//import com.inlaco.crewmgrservice.feature.apikey.application.port.in.ApiKeyUseCase;
//import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.ShipScheduleUseCase;
//import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
//import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.ScheduleStatus;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.time.Instant;
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureWebMvc
//@ActiveProfiles("test")
//@Transactional
//class ShipScheduleExternalApiIntegrationTest {
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    @Autowired
//    private ApiKeyUseCase apiKeyUseCase;
//
//    @Autowired
//    private ShipScheduleUseCase shipScheduleUseCase;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private MockMvc mockMvc;
//    private ApiKey testApiKey;
//    private ShipSchedule testSchedule;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//
//        // Create test API key
//        testApiKey = apiKeyUseCase.createApiKey("Test Client", "Integration Test API Key", "test-user");
//
//        // Create test ship schedule
//        testSchedule = shipScheduleUseCase.createSchedule(
//                ShipSchedule.builder()
//                        .clientId("client-123")
//                        .shipImo("ship-456")
//                        .shipName("Integration Test Ship")
//                        .route("Test Route")
//                        .departureTime(Instant.now().plusSeconds(3600))
//                        .arrivalTime(Instant.now().plusSeconds(7200))
//                        .departurePort("Test Port A")
//                        .arrivalPort("Test Port B")
//                        .status(ScheduleStatus.CONFIRMED)
//                        .employeeCardIds(List.of("emp-001", "emp-002"))
//                        .build()
//        );
//    }
//
//    @Test
//    void shouldCreateApiKeyAndAccessExternalApi() throws Exception {
//        // Test API key creation
//        mockMvc.perform(post("/api/v1/api-keys")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"clientName\":\"Integration Test Client\",\"description\":\"Test API Key\"}")
//                        .header("Authorization", "Bearer test-token")) // Mock admin token
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.clientName").value("Integration Test Client"))
//                .andExpect(jsonPath("$.keyId").exists())
//                .andExpect(jsonPath("$.keySecret").exists())
//                .andExpect(jsonPath("$.active").value(true));
//    }
//
//    @Test
//    void shouldAccessShipScheduleExternalApiWithApiKey() throws Exception {
//        // Test accessing external API with valid API key
//        mockMvc.perform(get("/api/v1/external/ship-schedules")
//                        .header("X-API-Key-ID", testApiKey.getKeyId())
//                        .header("X-API-Key-Secret", testApiKey.getKeySecret())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$[?(@.id == '" + testSchedule.getId() + "')].shipName")
//                        .value("Integration Test Ship"));
//    }
//
//    @Test
//    void shouldGetSpecificScheduleCrewInfo() throws Exception {
//        // Test getting crew info for specific schedule
//        mockMvc.perform(get("/api/v1/external/ship-schedules/{scheduleId}/crew", testSchedule.getId())
//                        .header("X-API-Key-ID", testApiKey.getKeyId())
//                        .header("X-API-Key-Secret", testApiKey.getKeySecret())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(testSchedule.getId()))
//                .andExpect(jsonPath("$.shipName").value("Integration Test Ship"))
//                .andExpect(jsonPath("$.status").value("CONFIRMED"))
//                .andExpect(jsonPath("$.crewMembers").isArray());
//    }
//
//    @Test
//    void shouldRejectInvalidApiKey() throws Exception {
//        // Test rejection with invalid API key
//        mockMvc.perform(get("/api/v1/external/ship-schedules")
//                        .header("X-API-Key-ID", "invalid-key")
//                        .header("X-API-Key-Secret", "invalid-secret")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void shouldRejectMissingApiKey() throws Exception {
//        // Test rejection without API key
//        mockMvc.perform(get("/api/v1/external/ship-schedules")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void shouldAccessHealthCheck() throws Exception {
//        // Test health check endpoint
//        mockMvc.perform(get("/api/v1/external/health")
//                        .header("X-API-Key-ID", testApiKey.getKeyId())
//                        .header("X-API-Key-Secret", testApiKey.getKeySecret())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string("API is accessible. Authenticated as: " + testApiKey.getClientName()));
//    }
//
//    @Test
//    void shouldReturn404ForNonExistentSchedule() throws Exception {
//        // Test 404 for non-existent schedule
//        mockMvc.perform(get("/api/v1/external/ship-schedules/non-existent/crew")
//                        .header("X-API-Key-ID", testApiKey.getKeyId())
//                        .header("X-API-Key-Secret", testApiKey.getKeySecret())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }
//}
