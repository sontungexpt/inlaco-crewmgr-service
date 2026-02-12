package com.inlaco.crewmgrservice.feature.schedule.application.mapper;

import com.inlaco.crewmgrservice.feature.schedule.application.model.AssignedCrewDetail;
import com.inlaco.crewmgrservice.feature.schedule.application.model.CrewMobilizationScheduleDetail;
import com.inlaco.crewmgrservice.feature.schedule.domain.model.CrewMobilizationSchedule;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CrewMobilizationScheduleDetailMapper {

  @Mapping(target = "crews", source = "crews")
  CrewMobilizationScheduleDetail toDetail(
      CrewMobilizationSchedule schedule, List<AssignedCrewDetail> crews);
}
