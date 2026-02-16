package com.inlaco.crewmgrservice.feature.contracttemplate.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.contracttemplate.domain.model.ContractTemplate;
import com.inlaco.crewmgrservice.feature.contracttemplate.infrastructure.persistence.mongodb.entity.ContractTemplateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring")
public interface ContractTemplateEntityMapper {

  ContractTemplate toContractTemplate(ContractTemplateEntity contractTemplateEntity);

  ContractTemplateEntity toContractTemplateEntity(ContractTemplate contractTemplate);

  void updateFromContractTemplate(
      ContractTemplate source, @MappingTarget ContractTemplateEntity target);
}
