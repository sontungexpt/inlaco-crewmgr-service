package com.inlaco.crewmgrservice.feature.post.domain.model;

import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.post.domain.enums.PostType;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.patch.JsonPatchIgnore;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public abstract class Post implements Serializable {

  @JsonPatchIgnore private String id;

  private String title;

  private String content;

  private String description;

  private List<File> attachments;

  private File image;

  private String company;

  private PostType type;

  @JsonPatchIgnore private String authorId;

  @JsonPatchIgnore private Instant createdAt;

  @JsonPatchIgnore private Instant updatedAt;

  public boolean isActive() {
    return true;
  }
}
