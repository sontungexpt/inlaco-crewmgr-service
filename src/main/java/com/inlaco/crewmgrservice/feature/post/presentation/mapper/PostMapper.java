package com.inlaco.crewmgrservice.feature.post.presentation.mapper;

import com.inlaco.crewmgrservice.feature.post.domain.model.EventPost;
import com.inlaco.crewmgrservice.feature.post.domain.model.NewsPost;
import com.inlaco.crewmgrservice.feature.post.domain.model.Post;
import com.inlaco.crewmgrservice.feature.post.domain.model.RecruitmentPost;
import com.inlaco.crewmgrservice.feature.post.presentation.dto.EventPostDTO;
import com.inlaco.crewmgrservice.feature.post.presentation.dto.NewsPostDTO;
import com.inlaco.crewmgrservice.feature.post.presentation.dto.PostDTO;
import com.inlaco.crewmgrservice.feature.post.presentation.dto.RecruitmentPostDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {

  default PostDTO toPostDTO(Post post) {
    return switch (post) {
      case NewsPost p -> toPostDTO(p);
      case RecruitmentPost p -> toPostDTO(p);
      case EventPost p -> toPostDTO(p);
      default -> throw new IllegalArgumentException("Unknown Post type: " + post.getClass());
    };
  }

  NewsPostDTO toPostDTO(NewsPost post);

  RecruitmentPostDTO toPostDTO(RecruitmentPost post);

  EventPostDTO toPostDTO(EventPost post);

  default Post toPost(PostDTO dto) {
    return switch (dto) {
      case NewsPostDTO p -> toPost(p);
      case RecruitmentPostDTO p -> toPost(p);
      case EventPostDTO p -> toPost(p);
      default -> throw new IllegalArgumentException("Unknown PostDTO type: " + dto.getClass());
    };
  }

  NewsPost toPost(NewsPostDTO dto);

  RecruitmentPost toPost(RecruitmentPostDTO dto);

  EventPost toPost(EventPostDTO dto);
}
