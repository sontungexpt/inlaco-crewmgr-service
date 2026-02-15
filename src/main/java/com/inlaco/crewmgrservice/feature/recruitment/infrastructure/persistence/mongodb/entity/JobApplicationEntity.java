package com.inlaco.crewmgrservice.feature.recruitment.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.common.model.Asset;
import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import com.inlaco.crewmgrservice.feature.user.domain.enums.Gender;
import java.time.Instant;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "applications")
@CompoundIndex(
    name = "post_status_applied_idx",
    def = "{'recruitmentPostId': 1, 'status': 1, 'appliedAt': -1}")
public class JobApplicationEntity {

  @Id private String id;

  private ObjectId accountId;

  private ObjectId recruitmentPostId;

  // Personal info
  private String fullName;

  private String email;

  private String phoneNumber;

  private String address;

  private Gender gender;

  private String languageSkills;

  private String experiences;

  private Asset resume;

  private String position;

  private ApplicationStatus status;

  // Auditing
  @CreatedDate private Instant appliedAt;

  @LastModifiedDate private Instant updatedAt;

  @CreatedBy private ObjectId createdBy;

  @LastModifiedBy private ObjectId updatedBy;
}
