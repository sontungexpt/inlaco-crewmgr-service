package com.inlaco.crewmgrservice.feature.post.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.inlaco.crewmgrservice.feature.post.enums.PostType;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "posts")
@JsonTypeInfo(
    include = JsonTypeInfo.As.PROPERTY,
    visible = true,
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    defaultImpl = BasicPost.class)
@JsonIgnoreProperties(
    value = {"id", "authorId", "createdAt", "updatedAt"},
    allowGetters = true)
public abstract class Post {

  @Id private String id;

  @NotBlank protected String title;

  @NotBlank protected String content;

  protected String description;

  protected String imageUrl;

  protected String company;

  protected PostType type;

  @CreatedBy private String authorId;

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;
}
