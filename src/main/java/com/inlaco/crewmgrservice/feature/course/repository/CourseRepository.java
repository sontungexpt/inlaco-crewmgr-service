package com.inlaco.crewmgrservice.feature.course.repository;

import com.inlaco.crewmgrservice.feature.course.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {

  Page<Course> findByDeleted(boolean isDeleted, Pageable pageable);

  Page<Course> findByDeletedAndNameContainingIgnoreCase(
      boolean isDeleted, String name, Pageable pageable);

  @Aggregation("{ $match: { _id: ?0 } }, { $set: { deleted: true } }")
  void shortDeleteById(String id);
}
