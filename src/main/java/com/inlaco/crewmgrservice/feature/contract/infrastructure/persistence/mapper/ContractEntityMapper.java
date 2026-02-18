package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.mapper;

import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.CrewSupplyContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity.contract.ContractEntity;
import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity.contract.CrewSupplyContractEntity;
import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity.contract.LaborContractEntity;
import com.inlaco.crewmgrservice.shared.mapstruct.config.CentralMapperConfig;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(
    componentModel = "spring",
    config = CentralMapperConfig.class,
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface ContractEntityMapper {

  static final int CURRENT_SCHEMA_VERSION = 1;

  @SubclassMapping(source = LaborContractEntity.class, target = LaborContract.class)
  @SubclassMapping(source = CrewSupplyContractEntity.class, target = CrewSupplyContract.class)
  Contract toContract(ContractEntity entity);

  @InheritConfiguration(name = "toContract")
  LaborContract toLaborContract(LaborContractEntity entity);

  @InheritConfiguration(name = "toContract")
  CrewSupplyContract toCrewSupplyContract(CrewSupplyContractEntity entity);

  // ===================== Contract to Entity =====================
  @SubclassMapping(source = LaborContract.class, target = LaborContractEntity.class)
  @SubclassMapping(source = CrewSupplyContract.class, target = CrewSupplyContractEntity.class)
  @Mapping(target = "schemaVersion", expression = "java(CURRENT_SCHEMA_VERSION)")
  ContractEntity toContractEntity(Contract contract);

  @InheritConfiguration(name = "toContractEntity")
  LaborContractEntity toLaborContractEntity(LaborContract contract);

  @InheritConfiguration(name = "toContractEntity")
  CrewSupplyContractEntity toCrewSupplyContractEntity(CrewSupplyContract contract);

  @InheritConfiguration(name = "toContractEntity")
  void updateFromContract(Contract contract, @MappingTarget ContractEntity entity);
}
