package com.inlaco.crewmgrservice.feature.course.application.port.out;

import com.inlaco.crewmgrservice.feature.course.application.model.CourseSearchCriteria;
import com.inlaco.crewmgrservice.feature.course.domain.model.Course;
import com.inlaco.crewmgrservice.feature.course.domain.model.UserCourse;
import java.util.Optional;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository {

  Page<Course> findAll(Pageable pageable);

  Page<UserCourse> findAllEnrolled(String userId, Pageable pageable);

  Page<Course> findAll(@Nullable CourseSearchCriteria criteria, Pageable pageable);

  void deleteById(String id);

  Optional<Course> findById(String id);

  Course save(Course course);
}
