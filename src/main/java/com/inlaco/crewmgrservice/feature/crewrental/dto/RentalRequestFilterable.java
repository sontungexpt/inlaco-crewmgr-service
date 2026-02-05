package com.inlaco.crewmgrservice.feature.crewrental.dto;

import com.inlaco.crewmgrservice.feature.crewrental.enums.RentalRequestStatus;
import com.inlaco.crewmgrservice.infrastructure.web.payload.Filterable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RentalRequestFilterable implements Filterable {

  private String keyword;

  private RentalRequestStatus status;
}
