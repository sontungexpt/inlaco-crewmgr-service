package com.inlaco.crewmgrservice.feature.shipschedule.presentation.mapper;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response.ShipScheduleCrewResponse;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.crew.presentation.dto.response.CrewProfileResponse;
import com.inlaco.crewmgrservice.feature.crew.presentation.mapper.CrewProfileMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = CrewProfileMapper.class)
public interface ShipScheduleCrewResponseMapper {
  
  @Mapping(target = "crewMembers", source = "employeeCardIds", qualifiedByName = "mapCrewMembers")
  ShipScheduleCrewResponse toResponse(ShipSchedule domain, List<CrewProfile> crewProfiles);
  
  @Named("mapCrewMembers")
  default List<CrewProfileResponse> mapCrewMembers(List<String> employeeCardIds, List<CrewProfile> crewProfiles, CrewProfileMapper crewProfileMapper) {
    return crewProfiles.stream()
        .filter(crew -> employeeCardIds.contains(crew.getEmployeeCardId()))
        .map(crewProfileMapper::toCrewProfileResponse)
        .toList();
  }
  
  List<ShipScheduleCrewResponse> toResponseList(List<ShipSchedule> domains, List<List<CrewProfile>> crewProfilesList);
}
