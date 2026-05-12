//package com.inlaco.crewmgrservice.feature.ship.integration;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.inlaco.crewmgrservice.feature.ship.domain.model.Ship;
//import com.inlaco.crewmgrservice.feature.ship.infrastructure.persistence.mongodb.entity.ShipEntity;
//import com.inlaco.crewmgrservice.feature.ship.infrastructure.persistence.mongodb.repository.ShipMongoRepository;
//import com.inlaco.crewmgrservice.feature.ship.presentation.rest.dto.request.CreateShipRequest;
//import com.inlaco.crewmgrservice.feature.ship.presentation.rest.dto.response.ShipResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.List;
//
//import static org.hamcrest.Matchers.*;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureWebMvc
//@ActiveProfiles("test")
//public class ShipControllerIntegrationTest {
//
//  @Autowired
//  private WebApplicationContext webApplicationContext;
//
//  @Autowired
//  private ShipMongoRepository shipMongoRepository;
//
//  @Autowired
//  private ObjectMapper objectMapper;
//
//  private MockMvc mockMvc;
//
//  @BeforeEach
//  void setUp() {
//    mockMvc = MockMvcBuilders
//        .webAppContextSetup(webApplicationContext)
//        .apply(springSecurity())
//        .build();
//
//    shipMongoRepository.deleteAll();
//  }
//
//  @Test
//  @WithMockUser(roles = {"ADMIN"})
//  void shouldCreateShipSuccessfully() throws Exception {
//    CreateShipRequest request = CreateShipRequest.builder()
//        .name("Test Ship")
//        .imoNumber("1234567X")
//        .callSign("ABCD")
//        .mmsi("123456789")
//        .flag("Panama")
//        .portOfRegistry("Panama City")
//        .shipType("Container Ship")
//        .classificationSociety("DNV")
//        .yearBuilt(2020)
//        .shipyard("Samsung Heavy Industries")
//        .deadweight(50000.0)
//        .grossTonnage(30000.0)
//        .netTonnage(15000.0)
//        .lengthOverall(200.0)
//        .beam(32.0)
//        .draft(12.0)
//        .engineType("Diesel")
//        .enginePower(15000.0)
//        .fuelType("Marine Diesel")
//        .maximumCrewCapacity(25)
//        .ownerCompanyId("company123")
//        .description("Test ship description")
//        .build();
//
//    mockMvc.perform(post("/api/v1/ships")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(request)))
//        .andExpect(status().isCreated())
//        .andExpect(jsonPath("$.name").value("Test Ship"))
//        .andExpect(jsonPath("$.imoNumber").value("1234567X"))
//        .andExpect(jsonPath("$.status").value("ACTIVE"))
//        .andExpect(jsonPath("$.maximumCrewCapacity").value(25))
//        .andExpect(jsonPath("$.currentCrewCount").value(0));
//  }
//
//  @Test
//  @WithMockUser(roles = {"ADMIN"})
//  void shouldReturnBadRequestWhenCreatingShipWithDuplicateImoNumber() throws Exception {
//    // Create first ship
//    ShipEntity ship = ShipEntity.builder()
//        .name("Existing Ship")
//        .imoNumber("9876543X")
//        .status(Ship.ShipStatus.ACTIVE)
//        .build();
//    shipMongoRepository.save(ship);
//
//    CreateShipRequest request = CreateShipRequest.builder()
//        .name("New Ship")
//        .imoNumber("9876543X")
//        .build();
//
//    mockMvc.perform(post("/api/v1/ships")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(request)))
//        .andExpect(status().isConflict());
//  }
//
//  @Test
//  void shouldGetShipsSuccessfully() throws Exception {
//    // Create test ships
//    ShipEntity ship1 = ShipEntity.builder()
//        .name("Ship A")
//        .imoNumber("1111111X")
//        .status(Ship.ShipStatus.ACTIVE)
//        .build();
//
//    ShipEntity ship2 = ShipEntity.builder()
//        .name("Ship B")
//        .imoNumber("2222222X")
//        .status(Ship.ShipStatus.ACTIVE)
//        .build();
//
//    shipMongoRepository.saveAll(List.of(ship1, ship2));
//
//    mockMvc.perform(get("/api/v1/ships"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.content", hasSize(2)))
//        .andExpect(jsonPath("$.content[*].name", containsInAnyOrder("Ship A", "Ship B")));
//  }
//
//  @Test
//  void shouldGetShipByIdSuccessfully() throws Exception {
//    ShipEntity ship = ShipEntity.builder()
//        .name("Test Ship")
//        .imoNumber("1234567X")
//        .status(Ship.ShipStatus.ACTIVE)
//        .build();
//
//    ShipEntity saved = shipMongoRepository.save(ship);
//
//    mockMvc.perform(get("/api/v1/ships/" + saved.getId()))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.name").value("Test Ship"))
//        .andExpect(jsonPath("$.imoNumber").value("1234567X"));
//  }
//
//  @Test
//  @WithMockUser(roles = {"ADMIN"})
//  void shouldUpdateShipSuccessfully() throws Exception {
//    ShipEntity ship = ShipEntity.builder()
//        .name("Original Name")
//        .imoNumber("1234567X")
//        .status(Ship.ShipStatus.ACTIVE)
//        .build();
//
//    ShipEntity saved = shipMongoRepository.save(ship);
//
//    CreateShipRequest updateRequest = CreateShipRequest.builder()
//        .name("Updated Name")
//        .imoNumber("1234567X")
//        .maximumCrewCapacity(30)
//        .build();
//
//    mockMvc.perform(put("/api/v1/ships/" + saved.getId())
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(updateRequest)))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.name").value("Updated Name"))
//        .andExpect(jsonPath("$.maximumCrewCapacity").value(30));
//  }
//
//  @Test
//  @WithMockUser(roles = {"ADMIN"})
//  void shouldDeleteShipSuccessfully() throws Exception {
//    ShipEntity ship = ShipEntity.builder()
//        .name("Test Ship")
//        .imoNumber("1234567X")
//        .status(Ship.ShipStatus.INACTIVE)
//        .build();
//
//    ShipEntity saved = shipMongoRepository.save(ship);
//
//    mockMvc.perform(delete("/api/v1/ships/" + saved.getId()))
//        .andExpect(status().isNoContent());
//  }
//
//  @Test
//  void shouldGetActiveShipsSuccessfully() throws Exception {
//    ShipEntity activeShip = ShipEntity.builder()
//        .name("Active Ship")
//        .imoNumber("1111111X")
//        .status(Ship.ShipStatus.ACTIVE)
//        .build();
//
//    ShipEntity inactiveShip = ShipEntity.builder()
//        .name("Inactive Ship")
//        .imoNumber("2222222X")
//        .status(Ship.ShipStatus.INACTIVE)
//        .build();
//
//    shipMongoRepository.saveAll(List.of(activeShip, inactiveShip));
//
//    mockMvc.perform(get("/api/v1/ships/active"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$", hasSize(1)))
//        .andExpect(jsonPath("$[0].name").value("Active Ship"));
//  }
//
//  @Test
//  void shouldGetAvailableShipsSuccessfully() throws Exception {
//    ShipEntity availableShip = ShipEntity.builder()
//        .name("Available Ship")
//        .imoNumber("1111111X")
//        .status(Ship.ShipStatus.ACTIVE)
//        .build();
//
//    ShipEntity maintenanceShip = ShipEntity.builder()
//        .name("Maintenance Ship")
//        .imoNumber("2222222X")
//        .status(Ship.ShipStatus.UNDER_MAINTENANCE)
//        .build();
//
//    shipMongoRepository.saveAll(List.of(availableShip, maintenanceShip));
//
//    mockMvc.perform(get("/api/v1/ships/available"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$", hasSize(1)))
//        .andExpect(jsonPath("$[0].name").value("Available Ship"));
//  }
//
//  @Test
//  void shouldCheckShipAvailabilitySuccessfully() throws Exception {
//    ShipEntity activeShip = ShipEntity.builder()
//        .name("Active Ship")
//        .imoNumber("1111111X")
//        .status(Ship.ShipStatus.ACTIVE)
//        .build();
//
//    ShipEntity saved = shipMongoRepository.save(activeShip);
//
//    mockMvc.perform(get("/api/v1/ships/" + saved.getId() + "/availability"))
//        .andExpect(status().isOk())
//        .andExpect(content().string("true"));
//  }
//
//  @Test
//  @WithMockUser(roles = {"ADMIN"})
//  void shouldUpdateCrewCountSuccessfully() throws Exception {
//    ShipEntity ship = ShipEntity.builder()
//        .name("Test Ship")
//        .imoNumber("1234567X")
//        .status(Ship.ShipStatus.ACTIVE)
//        .currentCrewCount(10)
//        .maximumCrewCapacity(25)
//        .build();
//
//    ShipEntity saved = shipMongoRepository.save(ship);
//
//    mockMvc.perform(patch("/api/v1/ships/" + saved.getId() + "/crew-count")
//            .param("crewCount", "15"))
//        .andExpect(status().isOk());
//  }
//
//  @Test
//  @WithMockUser(roles = {"ADMIN"})
//  void shouldChangeShipStatusSuccessfully() throws Exception {
//    ShipEntity ship = ShipEntity.builder()
//        .name("Test Ship")
//        .imoNumber("1234567X")
//        .status(Ship.ShipStatus.ACTIVE)
//        .build();
//
//    ShipEntity saved = shipMongoRepository.save(ship);
//
//    mockMvc.perform(patch("/api/v1/ships/" + saved.getId() + "/status")
//            .param("status", "UNDER_MAINTENANCE"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.status").value("UNDER_MAINTENANCE"));
//  }
//}
