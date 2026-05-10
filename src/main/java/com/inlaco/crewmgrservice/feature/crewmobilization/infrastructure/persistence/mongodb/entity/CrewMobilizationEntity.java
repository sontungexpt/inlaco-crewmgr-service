package com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.enums.CrewMobilizationStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.ShipInfo;
import java.time.Instant;
import java.util.Set;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "crew_mobilization_schedules")
@Data
public class CrewMobilizationEntity {

  @Id private String id;

  private String partnerName;

  private String partnerPhone;

  private String partnerEmail;

  private String partnerAddress;

  private ShipInfo shipInfo;

  private Instant startDate;

  private Instant endDate;

  private CrewMobilizationStatus status;

  private Set<CrewMobilizationAssignmentEntity> crews;

  @CreatedBy private ObjectId createdBy;
  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;
  @LastModifiedBy private ObjectId updatedBy;
}
