package com.inlaco.crewmgrservice.feature.crew.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import com.inlaco.crewmgrservice.shared.objectvalue.Gender;
import java.time.Instant;
import java.util.List;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.Document;

@CompoundIndexes({
  @CompoundIndex(
      name = "idx_status_position_createdAt",
      def = "{'workStatus': 1, 'professionalPosition': 1, 'createdAt': -1}")
})
@Data
@Document(collection = "crew_profiles")
public class CrewProfileEntity {

  @Id private String id;

  // 1 account = 1 profile
  @Indexed(unique = true)
  private ObjectId accountId;

  // Text search
  @TextIndexed(weight = 3)
  private String fullName;

  @TextIndexed(weight = 2)
  private String email;

  @TextIndexed(weight = 1)
  private String phoneNumber;

  private String address;

  private Gender gender;

  private CrewStatus status;

  private String professionalPosition;

  private Instant birthDate;

  @Indexed(unique = true, sparse = true)
  private String employeeCardId;

  private String socialInsuranceCode;

  private List<Asset> socialInsuranceImages;

  private String accidentInsuranceCode;

  private List<Asset> accidentInsuranceImages;

  // Sort list
  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;

  @CreatedBy private ObjectId createdBy;

  @LastModifiedBy private ObjectId updatedBy;
}
