package com.inlaco.crewmgrservice.feature.crewmobilization.application.model;

import com.inlaco.crewmgrservice.shared.objectvalue.Gender;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class AssignedCrewDetail {

  private String id;
  private String employeeCardId;
  private String accountId;

  private String rankOnBoard;
  private Instant startDate;
  private Instant endDate;
  private String remark;

  // profile info
  private String fullName;
  private String email;
  private String phoneNumber;
  private String address;
  private Gender gender;
  private List<String> professionalPositions;
}
