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
import org.mapstruct.ObjectFactory;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

  @ObjectFactory
  default PostDTO createDTO(Post post) {
    return switch (post) {
      case NewsPost p -> new NewsPostDTO();
      case RecruitmentPost p -> new RecruitmentPostDTO();
      case EventPost p -> new EventPostDTO();
      default -> throw new IllegalArgumentException("Unsupported post type: " + post.getClass());
    };
  }

  @ObjectFactory
  default Post createDomain(PostDTO dto) {
    return switch (dto) {
      case NewsPostDTO p -> new NewsPost();
      case RecruitmentPostDTO p -> new RecruitmentPost();
      case EventPostDTO p -> new EventPost();
      default -> throw new IllegalArgumentException("Unsupported post type: " + dto.getClass());
    };
  }

  @SubclassMapping(source = NewsPost.class, target = NewsPostDTO.class)
  @SubclassMapping(source = RecruitmentPost.class, target = RecruitmentPostDTO.class)
  @SubclassMapping(source = EventPost.class, target = EventPostDTO.class)
  PostDTO toDTO(Post post);

  @SubclassMapping(source = NewsPostDTO.class, target = NewsPost.class)
  @SubclassMapping(source = RecruitmentPostDTO.class, target = RecruitmentPost.class)
  @SubclassMapping(source = EventPostDTO.class, target = EventPost.class)
  Post toDomain(PostDTO entity);
}
