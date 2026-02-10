package com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.adapter;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.feature.course.application.port.out.CourseMemberRepository;
import com.inlaco.crewmgrservice.feature.course.domain.model.CourseMember;
import com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.entity.CourseMemberEntity;
import com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.mapper.CourseMemberEntityMapper;
import com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.repository.CourseMemberMongoRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CourseMemberRepositoryAdapter implements CourseMemberRepository {

  private final CourseMemberMongoRepository courseMemberMongoRepository;
  private final MongoTemplate mongoTemplate;
  private final CourseMemberEntityMapper mapper;

  @Override
  public long countByCourseId(String courseId) {
    return courseMemberMongoRepository.countByCourseId(new ObjectId(courseId));
  }

  @Override
  public boolean existsByCourseIdAndUserId(String courseId, String userId) {
    return courseMemberMongoRepository.existsByCourseIdAndUserId(
        new ObjectId(courseId), new ObjectId(userId));
  }

  @Override
  public Optional<CourseMember> findByCourseIdAndUserId(String courseId, String userId) {
    return courseMemberMongoRepository
        .findByCourseIdAndUserId(new ObjectId(courseId), new ObjectId(userId))
        .map(mapper::toDomain);
  }

  @Override
  public List<CourseMember> findByCourseId(String courseId) {
    return courseMemberMongoRepository.findByCourseId(new ObjectId(courseId)).stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public CourseMember save(CourseMember courseMember) {
    return mapper.toDomain(courseMemberMongoRepository.save(mapper.totEntity(courseMember)));
  }

  @Override
  public List<CourseMember> saveAll(Iterable<CourseMember> courseMember) {
    if (courseMember == null) {
      return Collections.emptyList();
    }

    Collection<CourseMemberEntity> courseMemberEntities = new ArrayList<>();
    courseMember.forEach((it) -> courseMemberEntities.add(mapper.totEntity(it)));

    return courseMemberMongoRepository.saveAll(courseMemberEntities).stream()
        .map(mapper::toDomain)
        .toList();
  }
}
