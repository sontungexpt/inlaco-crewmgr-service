package com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.course.application.port.out.CourseMemberRepository;
import com.inlaco.crewmgrservice.feature.course.domain.model.CourseMember;
import com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.entity.CourseMemberEntity;
import com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.mapper.CourseMemberEntityMapper;
import com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.repository.CourseMemberMongoRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    if (domains.isEmpty()) return Collections.emptyList();

    List<CourseMember> newDomains = new ArrayList<>();
    List<CourseMember> existingDomains = new ArrayList<>();

    // 1️⃣ Split new / existing
    for (CourseMember domain : domains) {
      if (domain.getId() == null) {
        newDomains.add(domain);
      } else {
        existingDomains.add(domain);
      }
    }

    List<CourseMember> result = new ArrayList<>(domains.size());

    // 2️⃣ Handle existing (update)
    if (!existingDomains.isEmpty()) {
      Map<String, CourseMemberEntity> existingMap =
          repository
              .findAllById(existingDomains.stream().map(CourseMember::getId).toList())
              .stream()
              .collect(Collectors.toMap(CourseMemberEntity::getId, Function.identity()));

      for (CourseMember domain : existingDomains) {
        CourseMemberEntity existing = existingMap.get(domain.getId());
        if (existing == null) {
          // If id exists but not in DB → treat as insert
          newDomains.add(domain);
        } else {
          mapper.updateFromDomain(domain, existing);
          result.add(mapper.toDomain(repository.save(existing)));
        }
      }
    }

    // 3️⃣ Bulk insert
    if (!newDomains.isEmpty()) {
      List<CourseMemberEntity> newEntities = newDomains.stream().map(mapper::toEntity).toList();
      result.addAll(repository.insert(newEntities).stream().map(mapper::toDomain).toList());
    }

    return result;
  }
}
