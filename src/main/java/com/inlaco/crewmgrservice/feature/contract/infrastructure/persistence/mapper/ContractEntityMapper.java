package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.mapper;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.CrewSupplyContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity.ContractEntity;
import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity.ContractSnapshotEntity;
import com.inlaco.crewmgrservice.shared.mapper.ObjectIdMapper;
import java.util.EnumMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@Component
@RequiredArgsConstructor
public class ContractEntityMapper implements ObjectIdMapper {

  private static final int CURRENT_SCHEMA_VERSION = 1;

  // ============================================================
  // Isolated & Safe ObjectMapper
  // ============================================================
  private static final ObjectMapper OBJECT_MAPPER =
      JsonMapper.builder()
          .changeDefaultPropertyInclusion(
              incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
          .changeDefaultPropertyInclusion(
              incl -> incl.withContentInclusion(JsonInclude.Include.NON_NULL))
          .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
          .changeDefaultVisibility(
              vc ->
                  vc.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                      .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                      .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE))
          .build();

  // ============================================================
  // ContractType Registry (EnumMap = faster & memory efficient)
  // ============================================================
  private static final Map<ContractType, Class<? extends AbstractContract>> TYPE_REGISTRY =
      new EnumMap<>(ContractType.class);

  static {
    TYPE_REGISTRY.put(ContractType.LABOR_CONTRACT, LaborContract.class);
    TYPE_REGISTRY.put(ContractType.SUPPLY_CONTRACT, CrewSupplyContract.class);
  }

  // ============================================================
  // DOMAIN -> ENTITY
  // ============================================================
  public ContractEntity toContractEntity(AbstractContract contract) {
    contract.refreshStatusIfNeeded();
    return ContractEntity.builder()
        .id(contract.getId())
        .type(contract.getType())
        .status(contract.getStatus())
        .activationDate(contract.getActivationDate())
        .expiredDate(contract.getExpiredDate())
        .version(contract.getVersion())
        .schemaVersion(CURRENT_SCHEMA_VERSION)
        .searchMeta(buildSearchMeta(contract))
        .payload(serialize(contract))
        .build();
  }

  // ============================================================
  // ENTITY -> DOMAIN
  // ============================================================
  public AbstractContract toContract(ContractEntity entity) {
    AbstractContract contract = deserialize(entity.getPayload(), entity.getType());
    contract.setId(entity.getId());
    contract.refreshStatusIfNeeded();
    return contract;
  }

  // ============================================================
  // SNAPSHOT
  // ============================================================
  public ContractSnapshotEntity toSnapshot(AbstractContract contract) {
    return ContractSnapshotEntity.builder()
        .contractId(map(contract.getId()))
        .status(contract.getStatus())
        .type(contract.getType())
        .version(contract.getVersion())
        .schemaVersion(CURRENT_SCHEMA_VERSION)
        .payload(serialize(contract))
        .build();
  }

  public AbstractContract toContract(ContractSnapshotEntity snapshot) {
    return deserialize(snapshot.getPayload(), snapshot.getType());
  }

  // ============================================================
  // INTERNAL
  // ============================================================
  private Map<String, Object> buildSearchMeta(AbstractContract contract) {

    return switch (contract.getType()) {
      case LABOR_CONTRACT -> {
        LaborContract labor = (LaborContract) contract;
        yield Map.of(
            "applicationId", labor.getApplicationId(),
            "employeeId", labor.getAccountId());
      }
      case SUPPLY_CONTRACT -> {
        CrewSupplyContract supply = (CrewSupplyContract) contract;
        yield Map.of("requestId", supply.getCrewRentalRequestId());
      }
    };
  }

  private Map<String, Object> serialize(AbstractContract contract) {
    return OBJECT_MAPPER.convertValue(contract, Map.class);
  }

  private AbstractContract deserialize(Map<String, Object> payload, ContractType type) {
    Class<? extends AbstractContract> clazz = TYPE_REGISTRY.get(type);
    if (clazz == null) throw new IllegalStateException("Unsupported ContractType: " + type);
    return OBJECT_MAPPER.convertValue(payload, clazz);
  }
}
