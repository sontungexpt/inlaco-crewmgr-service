package com.inlaco.crewmgrservice.feature.course.domain.model;

import com.inlaco.crewmgrservice.shared.application.model.Patch;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.time.Instant;
import lombok.Data;

@Data
public class CourseUpdateCommand {
  Patch<String> name;
  Patch<String> trainingProviderName;
  Patch<Asset> trainingProviderLogo;
  Patch<String> teacherName;
  Patch<String> archivedPosition;
  Patch<Boolean> certified;
  Patch<Asset> wallpaper;
  Patch<String> description;
  Patch<Instant> startRegistrationAt;
  Patch<Instant> endRegistrationAt;
  Patch<Instant> startDate;
  Patch<Instant> endDate;
  Patch<Integer> limitStudent;
}
