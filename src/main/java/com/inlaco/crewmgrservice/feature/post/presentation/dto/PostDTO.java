package com.inlaco.crewmgrservice.feature.post.presentation.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.inlaco.crewmgrservice.feature.post.domain.enums.PostType;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.patch.JsonPatchIgnoreProperties;
import com.inlaco.crewmgrservice.shared.objectvalue.AssetResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true)
@JsonSubTypes({
  @Type(value = NewsPostDTO.class, name = PostType.Fields.NEWS),
  @Type(value = RecruitmentPostDTO.class, name = PostType.Fields.RECRUITMENT),
  @Type(value = EventPostDTO.class, name = PostType.Fields.EVENT)
})
@JsonIgnoreProperties(
    value = {"id", "authorId", "createdAt", "updatedAt"},
    allowGetters = true)
@JsonPatchIgnoreProperties({"id", "authorId", "createdAt", "updatedAt"})
@Getter
@Setter
public abstract class PostDTO {

  private String id;

  @NotBlank private String title;

  @NotBlank private String content;

  private String description;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonAlias({"image"})
  private String imageAssetId;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private AssetResponse image;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonAlias({"attachments"})
  private List<String> attachmentAssetIds;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private List<AssetResponse> attachments;

  private String company;

  @NotNull private PostType type;

  private String authorId;

  private Instant updatedAt;

  protected PostDTO(PostType type) {
    this.type = type;
  }
}
