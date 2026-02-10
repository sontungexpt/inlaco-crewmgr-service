package com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.post.domain.model.EventPost;
import com.inlaco.crewmgrservice.feature.post.domain.model.NewsPost;
import com.inlaco.crewmgrservice.feature.post.domain.model.Post;
import com.inlaco.crewmgrservice.feature.post.domain.model.RecruitmentPost;
import com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.entity.EventPostEntity;
import com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.entity.NewsPostEntity;
import com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.entity.PostEntity;
import com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.entity.RecruitmentPostEntity;
import com.inlaco.crewmgrservice.shared.mapper.ObjectIdMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring", uses = ObjectIdMapper.class)
public interface PostEntityMapper {

  @ObjectFactory
  default PostEntity createEntity(Post post) {
    return switch (post) {
      case NewsPost p -> new NewsPostEntity();
      case RecruitmentPost p -> new RecruitmentPostEntity();
      case EventPost p -> new EventPostEntity();
      default -> throw new IllegalArgumentException("Unsupported post type: " + post.getClass());
    };
  }

  @ObjectFactory
  default Post createDomain(PostEntity entity) {
    return switch (entity) {
      case NewsPostEntity p -> new NewsPost();
      case RecruitmentPostEntity p -> new RecruitmentPost();
      case EventPostEntity p -> new EventPost();
      default -> throw new IllegalArgumentException("Unsupported post type: " + entity.getClass());
    };
  }

  @SubclassMapping(source = NewsPost.class, target = NewsPostEntity.class)
  @SubclassMapping(source = RecruitmentPost.class, target = RecruitmentPostEntity.class)
  @SubclassMapping(source = EventPost.class, target = EventPostEntity.class)
  @Mapping(target = "authorId", source = "authorId", qualifiedByName = "stringToObjectId")
  @Mapping(target = "id", source = "id", qualifiedByName = "stringToObjectId")
  PostEntity toPostEntity(Post post);

  @SubclassMapping(source = NewsPostEntity.class, target = NewsPost.class)
  @SubclassMapping(source = RecruitmentPostEntity.class, target = RecruitmentPost.class)
  @SubclassMapping(source = EventPostEntity.class, target = EventPost.class)
  @Mapping(target = "authorId", source = "authorId", qualifiedByName = "objectIdToString")
  @Mapping(target = "id", source = "id", qualifiedByName = "objectIdToString")
  Post toPost(PostEntity entity);
}
