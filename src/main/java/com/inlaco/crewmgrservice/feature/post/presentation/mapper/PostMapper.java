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

  // ===== Subtype mapping (MapStruct generate) =====

  NewsPostDTO toPostDTO(NewsPost post);

  RecruitmentPostDTO toPostDTO(RecruitmentPost post);

  EventPostDTO toPostDTO(EventPost post);

  NewsPost toPost(NewsPostDTO dto);

  RecruitmentPost toPost(RecruitmentPostDTO dto);

  EventPost toPost(EventPostDTO dto);

  // ===== Base mapping dùng default =====

  default PostDTO toPostDTO(Post post) {
    if (post == null) return null;

    if (post instanceof NewsPost news) {
      return toPostDTO(news);
    }
    if (post instanceof RecruitmentPost recruitment) {
      return toPostDTO(recruitment);
    }
    if (post instanceof EventPost event) {
      return toPostDTO(event);
    }

    throw new IllegalArgumentException("Unknown Post type: " + post.getClass());
  }

  default Post toPost(PostDTO dto) {
    if (dto == null) return null;

    if (dto instanceof NewsPostDTO news) {
      return toPost(news);
    }
    if (dto instanceof RecruitmentPostDTO recruitment) {
      return toPost(recruitment);
    }
    if (dto instanceof EventPostDTO event) {
      return toPost(event);
    }

    throw new IllegalArgumentException("Unknown PostDTO type: " + dto.getClass());
  }
}
