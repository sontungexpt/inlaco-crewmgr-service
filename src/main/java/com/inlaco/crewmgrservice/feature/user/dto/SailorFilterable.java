package com.inlaco.crewmgrservice.feature.user.dto;

import com.inlaco.crewmgrservice.feature.user.enums.WorkStatus;
import com.inlaco.crewmgrservice.infrastructure.web.payload.request.filter.Filterable;
import lombok.Data;

@Data
public class SailorFilterable implements Filterable {

  private String keyword;

  private String professionalPosition;

  private Boolean official;

  private WorkStatus workStatus;
}
