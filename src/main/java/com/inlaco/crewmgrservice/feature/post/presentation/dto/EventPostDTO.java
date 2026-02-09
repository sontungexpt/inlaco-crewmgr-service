package com.inlaco.crewmgrservice.feature.post.presentation.dto;

import com.inlaco.crewmgrservice.feature.post.presentation.dto.enums.PostType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventPostDTO extends PostDTO {

  public EventPostDTO() {
    super(PostType.EVENT);
  }
}
