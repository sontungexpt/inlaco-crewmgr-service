package com.inlaco.crewmgrservice.feature.contracttemplate.presentation.rest.mapper;

import com.inlaco.crewmgrservice.feature.contracttemplate.domain.model.ContractTemplate;
import com.inlaco.crewmgrservice.feature.contracttemplate.presentation.rest.dto.request.NewContractTemplate;
import com.inlaco.crewmgrservice.feature.contracttemplate.presentation.rest.dto.response.ContractTemplateResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContractTemplateMapper {

  ContractTemplateResponse toContractTemplateResponse(ContractTemplate contractTemplate);

  ContractTemplate toContractTemplate(NewContractTemplate newContractTemplate);
}
