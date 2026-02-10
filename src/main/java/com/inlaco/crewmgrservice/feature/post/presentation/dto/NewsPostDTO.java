package com.inlaco.crewmgrservice.feature.post.presentation.dto;

import com.inlaco.crewmgrservice.feature.post.domain.enums.PostType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsPostDTO extends PostDTO {

  public NewsPostDTO() {
    super(PostType.NEWS);
  }
}
