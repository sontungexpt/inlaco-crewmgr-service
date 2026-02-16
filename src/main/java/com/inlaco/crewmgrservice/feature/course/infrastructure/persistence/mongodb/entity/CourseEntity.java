package com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.entity;

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
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "courses")
public class CourseEntity {

  @Id private String id;

  private String name;

  private String trainingProviderName;

  private Asset trainingProviderLogo;

  private String teacherName;

  private String archivedPosition;

  private boolean certified;

  private Instant forciblyCanceledAt;

  @Indexed(partialFilter = "{ deletedAt: null }")
  private Instant deletedAt;

  private int limitStudent = Integer.MAX_VALUE;

  private int enrolledStudentCount = 0;

  private Asset wallpaper;

  private String description;

  private Instant manuallyRegistrationDisabledAt;

  private Instant startRegistrationAt;

  private Instant endRegistrationAt;

  private Instant startDate;

  private Instant endDate;

  @CreatedBy private ObjectId createdBy;

  @LastModifiedBy private ObjectId updatedBy;

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;
}
