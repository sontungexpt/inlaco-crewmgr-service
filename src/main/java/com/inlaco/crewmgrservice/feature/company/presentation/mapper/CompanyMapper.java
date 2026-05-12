package com.inlaco.crewmgrservice.feature.company.presentation.mapper;

import com.inlaco.crewmgrservice.feature.company.domain.model.Company;
import com.inlaco.crewmgrservice.feature.company.presentation.rest.dto.request.CreateCompanyRequest;
import com.inlaco.crewmgrservice.feature.company.presentation.rest.dto.request.UpdateCompanyRequest;
import com.inlaco.crewmgrservice.feature.company.presentation.rest.dto.response.CompanyResponse;
import com.inlaco.crewmgrservice.shared.mapstruct.mapper.AssetResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {AssetResponseMapper.class})
public interface CompanyMapper {
  CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Company toDomain(CreateCompanyRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Company toDomain(UpdateCompanyRequest request);

  CompanyResponse toResponse(Company company);
}
