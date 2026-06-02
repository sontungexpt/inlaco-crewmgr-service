package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.mapper;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.CrewSupplyContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity.ContractSnapshotEntity;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@Component
@RequiredArgsConstructor
public class ContractSnapshotEntityMapper {
  private static final int CURRENT_SCHEMA_VERSION = 1;

  private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

  private static final ObjectMapper OBJECT_MAPPER =
      JsonMapper.builder()
          .changeDefaultPropertyInclusion(
              incl ->
                  incl.withValueInclusion(JsonInclude.Include.NON_NULL)
                      .withContentInclusion(JsonInclude.Include.NON_NULL))
          .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
          .enable(MapperFeature.PROPAGATE_TRANSIENT_MARKER)
          .changeDefaultVisibility(
              vc ->
                  vc.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                      .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                      .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE))
          .build();

  private static final Map<ContractType, Class<? extends Contract>> TYPE_REGISTRY =
      new EnumMap<>(ContractType.class) {
        {
          put(ContractType.LABOR_CONTRACT, LaborContract.class);
          put(ContractType.SUPPLY_CONTRACT, CrewSupplyContract.class);
        }
      };

  public ContractSnapshotEntity toSnapshot(Contract contract) {
    return ContractSnapshotEntity.builder()
        .contractId(toObjectId(contract.getId()))
        .type(contract.getType())
        .status(contract.getStatus())
        .version(contract.getVersion())
        .schemaVersion(CURRENT_SCHEMA_VERSION)
        .searchMeta(buildSearchMeta(contract))
        .payload(serialize(contract))
        .build();
  }

  public Contract toContract(ContractSnapshotEntity snapshot) {
    return deserialize(snapshot.getPayload(), snapshot.getType());
  }

  private Map<String, Object> buildSearchMeta(Contract contract) {
    Map<String, Object> searchMeta = new HashMap<>();
    switch (contract) {
      case LaborContract labor -> {
        searchMeta.put("applicationId", labor.getApplicationId());
        searchMeta.put("accountId", labor.getAccountId());
      }
      case CrewSupplyContract supply -> {
        searchMeta.put("crewRentalRequestId", supply.getCrewRentalRequestId());
      }
      default -> {}
    }
    return searchMeta;
  }

  private Map<String, Object> serialize(Contract contract) {
    try {
      Map<String, Object> map = new HashMap<>(OBJECT_MAPPER.convertValue(contract, MAP_TYPE));
      // The snapshot always has newer version
      map.put("hasNewerVersion", true);
      return Collections.unmodifiableMap(map);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to serialize contract", e);
    }
  }

  private Contract deserialize(Map<String, Object> payload, ContractType type) {
    Class<? extends Contract> clazz = TYPE_REGISTRY.get(type);

    if (clazz == null) {
      throw new IllegalStateException("Unsupported ContractType: " + type);
    }

    try {
      Contract contract = OBJECT_MAPPER.convertValue(payload, clazz);
      // The snapshot always has newer version
      contract.setHasNewerVersion(true);
      return contract;
    } catch (Exception e) {
      throw new IllegalStateException("Failed to deserialize contract", e);
    }
  }

  private ObjectId toObjectId(String id) {
    return id != null ? new ObjectId(id) : null;
  }
}
