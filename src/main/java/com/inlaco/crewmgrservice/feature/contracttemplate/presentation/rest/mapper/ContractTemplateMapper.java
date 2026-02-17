package com.inlaco.crewmgrservice.feature.contracttemplate.presentation.rest.mapper;

import com.inlaco.crewmgrservice.feature.contracttemplate.domain.model.ContractTemplate;
import com.inlaco.crewmgrservice.feature.contracttemplate.presentation.rest.dto.request.NewContractTemplate;
import com.inlaco.crewmgrservice.feature.contracttemplate.presentation.rest.dto.response.ContractTemplateResponse;
import com.inlaco.crewmgrservice.shared.mapstruct.config.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    config = CentralMapperConfig.class)
public interface ContractTemplateMapper {

  ContractTemplateResponse toContractTemplateResponse(ContractTemplate contractTemplate);

  ContractTemplate toContractTemplate(NewContractTemplate newContractTemplate);
}
