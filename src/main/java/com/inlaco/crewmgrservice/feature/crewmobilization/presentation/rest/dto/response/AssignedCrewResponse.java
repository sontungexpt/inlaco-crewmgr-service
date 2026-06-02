package com.inlaco.crewmgrservice.feature.crewmobilization.presentation.rest.dto.response;

import com.inlaco.crewmgrservice.shared.objectvalue.Gender;
import java.time.Instant;
import lombok.Data;

@Data
public class AssignedCrewResponse {

  private String id;

  private String employeeCardId;

  private String rankOnBoard;

  private Instant startDate;

  private Instant endDate;

  private String remark;

  private String fullName;

  private String email;

  private String phoneNumber;

  private String address;

  private Gender gender;

  private String professionalPosition;
}
