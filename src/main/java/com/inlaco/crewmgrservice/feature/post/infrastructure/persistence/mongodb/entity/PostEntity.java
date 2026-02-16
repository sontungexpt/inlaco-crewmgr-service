package com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.common.model.Asset;
import com.inlaco.crewmgrservice.feature.post.domain.enums.PostType;
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
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@NoArgsConstructor
@Document(collection = "posts")
public abstract class PostEntity {

  @Id private String id;

  private String title;

  private String content;

  private String description;

  private List<Asset> attachments;

  private Asset image;

  private String company;

  private PostType type;

  private ObjectId deletedBy;

  @Indexed(partialFilter = "{ deletedAt: null }")
  private Instant deletedAt;

  @CreatedBy private ObjectId authorId;

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;
}
