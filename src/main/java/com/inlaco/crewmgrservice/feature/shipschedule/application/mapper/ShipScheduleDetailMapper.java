package com.inlaco.crewmgrservice.feature.shipschedule.application.mapper;

import com.inlaco.crewmgrservice.feature.shipschedule.application.model.ShipScheduleAssignedCrewDetail;
import com.inlaco.crewmgrservice.feature.shipschedule.application.model.ShipScheduleDetail;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShipScheduleDetailMapper {

  @Mapping(target = "crews", source = "crews")
  ShipScheduleDetail toDetail(ShipSchedule schedule, List<ShipScheduleAssignedCrewDetail> crews);
}
