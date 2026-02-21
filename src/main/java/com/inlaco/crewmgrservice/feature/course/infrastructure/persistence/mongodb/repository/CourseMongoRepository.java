package com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.entity.CourseEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseMongoRepository extends MongoRepository<CourseEntity, String> {

  Page<CourseEntity> findByDeletedAtIsNull(Pageable pageable);

  Optional<CourseEntity> findByIdAndDeletedAtIsNull(String id);
}
