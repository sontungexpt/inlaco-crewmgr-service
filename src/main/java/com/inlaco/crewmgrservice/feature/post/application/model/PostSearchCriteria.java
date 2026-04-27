package com.inlaco.crewmgrservice.feature.post.application.model;

import com.inlaco.crewmgrservice.feature.post.domain.enums.PostType;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class PostSearchCriteria {
  private String keyword;

  private PostType type;
}
