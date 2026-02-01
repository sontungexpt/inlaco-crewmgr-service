package com.inlaco.crewmgrservice.feature.user.dto;

import com.inlaco.crewmgrservice.common.payload.Filterable;
import com.inlaco.crewmgrservice.feature.user.enums.WorkStatus;
import lombok.Data;

@Data
public class SailorFilterable implements Filterable {

  private String professionalPosition;

  private Boolean official;

  private WorkStatus workStatus;
}
