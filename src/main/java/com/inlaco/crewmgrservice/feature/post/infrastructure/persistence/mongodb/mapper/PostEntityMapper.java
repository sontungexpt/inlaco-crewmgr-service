package com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.post.domain.model.EventPost;
import com.inlaco.crewmgrservice.feature.post.domain.model.NewsPost;
import com.inlaco.crewmgrservice.feature.post.domain.model.Post;
import com.inlaco.crewmgrservice.feature.post.domain.model.RecruitmentPost;
import com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.entity.EventPostEntity;
import com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.entity.NewsPostEntity;
import com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.entity.PostEntity;
import com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.entity.RecruitmentPostEntity;
import com.inlaco.crewmgrservice.shared.mapstruct.config.CentralMapperConfig;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    config = CentralMapperConfig.class,
    subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface PostEntityMapper {

  // =================== Mapping PostEntity to Post ==================
  @SubclassMapping(source = NewsPostEntity.class, target = NewsPost.class)
  @SubclassMapping(source = RecruitmentPostEntity.class, target = RecruitmentPost.class)
  @SubclassMapping(source = EventPostEntity.class, target = EventPost.class)
  Post toPost(PostEntity entity);

  // @InheritConfiguration(name = "toPost")
  // NewsPost toNewsPost(NewsPostEntity entity);

  // @InheritConfiguration(name = "toPost")
  // RecruitmentPost toRecruitmentPost(RecruitmentPostEntity entity);

  // @InheritConfiguration(name = "toPost")
  // EventPost toEventPost(EventPostEntity entity);

  // =================== Mapping Post to PostEntity ==================
  @SubclassMapping(source = NewsPost.class, target = NewsPostEntity.class)
  @SubclassMapping(source = RecruitmentPost.class, target = RecruitmentPostEntity.class)
  @SubclassMapping(source = EventPost.class, target = EventPostEntity.class)
  PostEntity toPostEntity(Post post);

  @InheritConfiguration(name = "toPostEntity")
  NewsPostEntity toNewsPostEntity(NewsPost post);

  @InheritConfiguration(name = "toPostEntity")
  RecruitmentPostEntity toRecruitmentPostEntity(RecruitmentPost post);

  @InheritConfiguration(name = "toPostEntity")
  EventPostEntity toEventPostEntity(EventPost post);

  // =================== Update PostEntity from Post ==================
  // NOTE: WAITING FOR PULL REQUEST MERGED
  default void updateFromPost(Post post, @MappingTarget PostEntity entity) {
    switch (post) {
      case NewsPost p -> updateFromPost(p, (NewsPostEntity) entity);
      case EventPost p -> updateFromPost(p, (EventPostEntity) entity);
      case RecruitmentPost p -> updateFromPost(p, (RecruitmentPostEntity) entity);
      default -> throw new IllegalArgumentException("Unsupported type");
    }
  }

  void updateFromPost(NewsPost post, @MappingTarget NewsPostEntity entity);

  void updateFromPost(EventPost post, @MappingTarget EventPostEntity entity);

  void updateFromPost(RecruitmentPost post, @MappingTarget RecruitmentPostEntity entity);
}
