package com.inlaco.crewmgrservice.feature.post.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.inlaco.crewmgrservice.feature.post.enums.PostType;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonTypeInfo(
    include = JsonTypeInfo.As.PROPERTY,
    visible = true,
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    defaultImpl = BasicPost.class)
@JsonIgnoreProperties(
    value = {"id", "authorId", "createdAt", "updatedAt"},
    allowGetters = true)
@Document(collection = "posts")
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public abstract class Post implements Serializable {

  @Id private String id;

  @NotBlank protected String title;

  @NotBlank protected String content;

  protected String description;

  protected String imageUrl;

  protected String company;

  protected PostType type;

  @CreatedBy private ObjectId authorId;

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;
}
