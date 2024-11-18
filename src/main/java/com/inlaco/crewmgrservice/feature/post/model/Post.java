package com.inlaco.crewmgrservice.feature.post.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.inlaco.crewmgrservice.annotation.JsonPatchIgnore;
import com.inlaco.crewmgrservice.feature.post.enums.PostType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
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

@Schema(description = "Represents a post with details such as title, content, author, etc.")
@JsonTypeInfo(
    include = JsonTypeInfo.As.PROPERTY,
    visible = true,
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    defaultImpl = NewsPost.class)
@JsonIgnoreProperties(
    value = {"id", "authorId", "createdAt", "updatedAt"},
    allowGetters = true)
@Document(collection = "posts")
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public abstract class Post implements Serializable {

  @Schema(
      description = "Unique identifier of the post",
      example = "60d5c88e4314e3b98a1e4fbd",
      hidden = true)
  @Id
  @JsonPatchIgnore
  private String id;

  @Schema(description = "Title of the post", example = "Introduction to Spring Boot")
  @NotBlank
  protected String title;

  @Schema(
      description = "Content of the post",
      example = "Spring Boot makes it easy to create stand-alone applications...")
  @NotBlank
  protected String content;

  @Schema(description = "Brief description of the post", example = "A quick guide on Spring Boot.")
  protected String description;

  @Schema(
      description = "List of tags associated with the post",
      example = "[\"Spring Boot\", \"Java\"]",
      hidden = true)
  private List<String> attachments;

  @Schema(
      description = "URL of the image associated with the post",
      example = "https://example.com/image.jpg")
  protected String imageUrl;

  @Schema(description = "Name of the company associated with the post", example = "Inlaco")
  protected String company;

  @Schema(description = "Type of the post", enumAsRef = true)
  protected PostType type;

  @Schema(
      description = "Author's unique identifier",
      example = "5f8d0d55b54764421b7156a2",
      hidden = true)
  @CreatedBy
  private ObjectId authorId;

  @Schema(
      description = "Date and time when the post was created",
      example = "2023-10-23T10:15:30Z",
      hidden = true)
  @CreatedDate
  @JsonIgnore
  private Instant createdAt;

  @Schema(
      description = "Date and time when the post was last updated",
      example = "2023-10-25T12:45:20Z",
      hidden = true)
  @LastModifiedDate
  private Instant updatedAt;
}
