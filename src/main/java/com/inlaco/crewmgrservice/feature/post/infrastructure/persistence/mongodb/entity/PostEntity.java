package com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.common.model.File;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@NoArgsConstructor
@Document(collection = "posts")
public abstract class PostEntity {

  @Id private ObjectId id;

  private String title;

  private String content;

  private String description;

  private List<File> attachments;

  private File image;

  private String company;

  private boolean deleted = false;

  private ObjectId deletedBy;

  private Instant deletedAt;

  @CreatedBy private ObjectId authorId;

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;
}
