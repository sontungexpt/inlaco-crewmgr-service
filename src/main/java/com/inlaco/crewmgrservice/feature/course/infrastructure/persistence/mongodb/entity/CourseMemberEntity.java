package com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.common.model.Asset;
import com.inlaco.crewmgrservice.feature.course.domain.enums.CourseMemberStatus;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "course_members")
public class CourseMemberEntity {

  @Id private String id;

  private ObjectId courseId;

  @Indexed private ObjectId userId;

  private CourseMemberStatus status; // The status of the course member

  private short completionProgress;

  private String note;

  private Instant completedAt;

  private Instant forciblyFinishedAt;

  private Instant cancelledAt;

  private Asset certificate;

  private Instant expiredAt;

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;
}
