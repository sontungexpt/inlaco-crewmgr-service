package com.inlaco.crewmgrservice.feature.post.domain.model;

import com.inlaco.crewmgrservice.common.model.File;
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

  protected String title;

  protected String content;

  protected String description;

  private List<File> attachments;

  protected File image;

  protected String company;

  @JsonPatchIgnore private String authorId;

  @JsonPatchIgnore private Instant createdAt;

  @JsonPatchIgnore private Instant updatedAt;

  public boolean isActive() {
    return true;
  }
}
