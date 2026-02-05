package com.inlaco.crewmgrservice.feature.course.model.dto;

import com.inlaco.crewmgrservice.infrastructure.web.payload.Filterable;
import lombok.Data;

@Data
public class CourseFilterable implements Filterable {
  String keyword;
  Boolean nonExpired = true;
}
