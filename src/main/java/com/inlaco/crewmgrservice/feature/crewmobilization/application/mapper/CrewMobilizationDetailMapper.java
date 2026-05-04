package com.inlaco.crewmgrservice.feature.crewmobilization.application.mapper;

import com.inlaco.crewmgrservice.feature.crewmobilization.application.model.AssignedCrewDetail;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.model.CrewMobilizationDetail;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilizationSchedule;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CrewMobilizationDetailMapper {

  @Mapping(target = "crews", source = "crews")
  CrewMobilizationDetail toDetail(
      CrewMobilizationSchedule schedule, List<AssignedCrewDetail> crews);
}
