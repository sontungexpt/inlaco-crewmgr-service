package com.inlaco.crewmgrservice.feature.crewhiring.dto;

import com.inlaco.crewmgrservice.common.payload.Filterable;
import com.inlaco.crewmgrservice.feature.crewhiring.enums.CrewRentalRequestStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CrewRentalRequestFilter implements Filterable {

  private CrewRentalRequestStatus status;
}
