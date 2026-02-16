package com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.adapter;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.feature.course.application.port.out.CourseMemberRepository;
import com.inlaco.crewmgrservice.feature.course.domain.model.CourseMember;
import com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.entity.CourseMemberEntity;
import com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.mapper.CourseMemberEntityMapper;
import com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.repository.CourseMemberMongoRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CourseMemberRepositoryAdapter implements CourseMemberRepository {

  private final CourseMemberMongoRepository repository;
  private final CourseMemberEntityMapper mapper;

  @Override
  public long countByCourseId(String courseId) {
    return repository.countByCourseId(new ObjectId(courseId));
  }

  @Override
  public boolean existsByCourseIdAndUserId(String courseId, String userId) {
    return repository.existsByCourseIdAndUserId(new ObjectId(courseId), new ObjectId(userId));
  }

  @Override
  public Optional<CourseMember> findByCourseIdAndUserId(String courseId, String userId) {
    return repository
        .findByCourseIdAndUserId(new ObjectId(courseId), new ObjectId(userId))
        .map(mapper::toDomain);
  }

  @Override
  public List<CourseMember> findByCourseId(String courseId) {
    return repository.findByCourseId(new ObjectId(courseId)).stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public CourseMember save(CourseMember courseMember) {
    String id = courseMember.getId();

    if (id == null) {
      // INSERT
      return mapper.toDomain(repository.insert(mapper.toEntity(courseMember)));
    }
    // UPDATE
    CourseMemberEntity entity =
        repository
            .findById(id)
            .map(
                existing -> {
                  mapper.updateFromDomain(courseMember, existing);
                  return existing;
                })
            // allow insert with custom id
            .orElseGet(() -> mapper.toEntity(courseMember));

    return mapper.toDomain(repository.save(entity));
  }

  @Override
  public List<CourseMember> saveAll(Iterable<CourseMember> courseMembers) {
    if (courseMembers == null) return Collections.emptyList();
    List<CourseMember> domains = StreamSupport.stream(courseMembers.spliterator(), false).toList();

    // Collect existing entities in batch
    Map<String, CourseMemberEntity> existingMap =
        repository
            .findAllById(
                domains.stream().map(CourseMember::getId).filter(Objects::nonNull).toList())
            .stream()
            .collect(Collectors.toMap(CourseMemberEntity::getId, Function.identity()));

    List<CourseMemberEntity> entities =
        domains.stream()
            .map(
                domain -> {
                  if (domain.getId() == null) {
                    return mapper.toEntity(domain); // new entity
                  }

                  CourseMemberEntity existing = existingMap.get(domain.getId());

                  if (existing == null) {
                    return mapper.toEntity(domain); // treat as insert
                  }

                  mapper.updateFromDomain(domain, existing); // merge changes
                  return existing;
                })
            .toList();

    return repository.saveAll(entities).stream().map(mapper::toDomain).toList();
  }
}
