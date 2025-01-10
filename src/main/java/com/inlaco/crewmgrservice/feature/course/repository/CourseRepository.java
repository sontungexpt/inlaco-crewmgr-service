package com.inlaco.crewmgrservice.feature.course.repository;

import com.inlaco.crewmgrservice.feature.course.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {

  Page<Course> findByIsDeleted(boolean isDeleted, Pageable pageable);

  Page<Course> findByIsDeletedAndNameContainingIgnoreCase(
      boolean isDeleted, String name, Pageable pageable);
}
