package com.inlaco.crewmgrservice.feature.crewrental.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.common.model.ShipInfo;
import com.inlaco.crewmgrservice.feature.crewrental.domain.enums.CrewRentalRequestStatus;
import java.time.Instant;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("crew_rental_requests")
@Data
public class CrewRentalRequestEntity {

  @Id private String id;

  private File detailFile;

  private String companyName;

  private String companyAddress;

  private String companyPhone;

  private String companyEmail;

  private String companyRepresentor;

  private String companyRepresentorPosition;

  private Instant rentalStartDate;

  private Instant rentalEndDate;

  private ShipInfo shipInfo;

  private ObjectId contractId;

  private CrewRentalRequestStatus status;

  private ObjectId reviewedBy;

  private Instant reviewedAt;

  @CreatedBy private ObjectId createdBy;

  @CreatedDate private Instant createdAt;

  @LastModifiedBy private ObjectId updatedBy;

  @LastModifiedDate private Instant updatedAt;
}
