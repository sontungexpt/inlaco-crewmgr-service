package com.inlaco.crewmgrservice.feature.course.model.dto;

import com.inlaco.crewmgrservice.common.payload.Filterable;
import lombok.Data;

@Data
public class CourseFilterable implements Filterable {
  String keyword;
  Boolean nonExpired = true;
}
