package com.inlaco.crewmgrservice.feature.post.model;

import com.inlaco.crewmgrservice.feature.post.enums.PostType;

/** PostFactory */
public class PostFactory {

  public Post createPost(PostType type) {
    switch (type) {
      case BASIC:
        return new BasicPost();
      case RECRUITMENT:
        return new RecruitmentPost();
      default:
        return null;
    }
  }
}
