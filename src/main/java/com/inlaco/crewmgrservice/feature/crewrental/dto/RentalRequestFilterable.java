package com.inlaco.crewmgrservice.feature.crewrental.dto;

import com.inlaco.crewmgrservice.common.payload.Filterable;
import com.inlaco.crewmgrservice.feature.crewrental.enums.RentalRequestStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RentalRequestFilterable implements Filterable {

  private RentalRequestStatus status;
}
