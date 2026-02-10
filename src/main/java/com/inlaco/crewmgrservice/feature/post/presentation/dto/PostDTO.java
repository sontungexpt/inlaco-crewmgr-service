package com.inlaco.crewmgrservice.feature.post.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.post.domain.enums.PostType;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.patch.JsonPatchIgnoreProperties;
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

  private List<File> attachments;

  private File image;

  private String company;

  @NotNull private PostType type;

  private String authorId;

  private Instant updatedAt;

  protected PostDTO(PostType type) {
    this.type = type;
  }
}
