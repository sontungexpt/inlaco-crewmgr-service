package com.inlaco.crewmgrservice.feature.post.domain.model;

import com.inlaco.crewmgrservice.feature.post.domain.enums.PostType;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
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

  private String id;

  private String title;
  private String content;
  private String description;

  private List<Asset> attachments;

  private Asset image;
  private String company;
  private PostType type;

  private String authorId;
  private Instant createdAt;
  private Instant updatedAt;

  public boolean isActive() {
    return true;
  }

  public boolean update(PostUpdateCommand command) {
    return command.getTitle().ifUpdated(this::setTitle)
        | command.getContent().ifUpdated(this::setContent)
        | command.getDescription().ifUpdated(this::setDescription)
        | command.getAttachments().ifUpdated(this::setAttachments)
        | command.getImage().ifUpdated(this::setImage)
        | command.getCompany().ifUpdated(this::setCompany);
  }
}
