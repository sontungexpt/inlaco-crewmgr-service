package com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.feature.course.domain.enums.CourseMemberStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "course_members")
@CompoundIndex(name = "uk_course_user", def = "{'courseId': 1, 'userId': 1}", unique = true)
@CompoundIndex(name = "idx_course_status", def = "{'courseId': 1, 'status': 1}")
public class CourseMemberEntity {

  @Id private String id;

  private ObjectId courseId;
  private ObjectId userId;

  private CourseMemberStatus status; // The status of the course member

  private short completionProgress;

  private String note;

  private Instant completedAt;

  private Instant forciblyFinishedAt;

  private Instant cancelledAt;

  private Asset certificate;

  private Instant expiredAt;

  @CreatedDate private Instant createdAt;

  @CreatedBy private ObjectId createdBy;

  @LastModifiedDate private Instant updatedAt;

  @LastModifiedBy private ObjectId updatedBy;
}
