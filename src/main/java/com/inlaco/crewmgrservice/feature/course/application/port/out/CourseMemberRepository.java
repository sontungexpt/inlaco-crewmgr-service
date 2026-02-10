package com.inlaco.crewmgrservice.feature.course.application.port.out;

import com.inlaco.crewmgrservice.feature.course.domain.model.CourseMember;
import java.util.List;
import java.util.Optional;

public interface CourseMemberRepository {

  CourseMember save(CourseMember courseMember);

  List<CourseMember> saveAll(Iterable<CourseMember> courseMember);

  long countByCourseId(String courseId);

  boolean existsByCourseIdAndUserId(String courseId, String userId);

  Optional<CourseMember> findByCourseIdAndUserId(String courseId, String userId);

  List<CourseMember> findByCourseId(String courseId);
}
