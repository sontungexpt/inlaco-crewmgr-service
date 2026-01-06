package com.inlaco.crewmgrservice.feature.user.dto;

import com.inlaco.crewmgrservice.common.payload.Filterable;
import com.inlaco.crewmgrservice.feature.user.enums.WorkStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SailorFilterable implements Filterable {

  private String professionalPosition;

  private Boolean official;

  private WorkStatus workStatus;
}
