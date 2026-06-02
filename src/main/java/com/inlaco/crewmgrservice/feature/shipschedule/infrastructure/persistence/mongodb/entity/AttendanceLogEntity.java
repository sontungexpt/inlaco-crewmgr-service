package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.AttendanceMethod;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.CheckType;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "attendance_logs")
public class AttendanceLogEntity {

  @Id private String id;

  private ObjectId crewAccountId;
  private ObjectId crewProfileId;
  private String crewEmployeeCardId;
  private String crewName;
  private String crewRankOnBoard;

  private String shipScheduleId;

  private Instant timestamp;

  private CheckType checkType;

  private AttendanceMethod method;

  private String location;
  private String deviceId;

  private String note;

  @CreatedBy private ObjectId createdBy;

  @CreatedDate private Instant createdAt;
}
