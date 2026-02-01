package com.inlaco.crewmgrservice.feature.crewrental.dto;

import com.inlaco.crewmgrservice.common.payload.Filterable;
import com.inlaco.crewmgrservice.feature.crewrental.enums.RentalRequestStatus;
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
