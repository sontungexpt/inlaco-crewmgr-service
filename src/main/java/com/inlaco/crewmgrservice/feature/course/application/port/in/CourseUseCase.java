package com.inlaco.crewmgrservice.feature.course.application.port.in;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.course.application.model.CourseSearchCriteria;
import com.inlaco.crewmgrservice.feature.course.domain.model.Course;
import com.inlaco.crewmgrservice.feature.course.domain.model.CourseMember;
import com.inlaco.crewmgrservice.feature.course.domain.model.UserCourse;
import com.inlaco.crewmgrservice.feature.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseUseCase {

  Page<Course> getCourses(CourseSearchCriteria filterable, Pageable pageable);

  Course getCourse(String id);

  UserCourse getUserCourse(String id, User user);

  Page<UserCourse> getEnrolledUserCourses(User user, Pageable pageable);

  Course createCourse(Course newCourse, String wallpaperAssetId, String logoAssetId);

  Course updateCourse(String id, JsonNode updatedPatch);

  void cancelCourse(String id);

  void cancelCourseRegistration(String id);

  void deleteCourse(String id);

  void updateEmployeeCompletionProgress(String sailorId);

  void enrollCourse(String courseId, User user);

  void markCourseCompleted(String courseId, String userId);

  Page<CourseMember> getCourseMembers(String courseId, Pageable pageable);
}
