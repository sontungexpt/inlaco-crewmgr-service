package com.inlaco.crewmgrservice.feature.course.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.course.model.Course;
import com.inlaco.crewmgrservice.feature.course.model.dto.CourseDetail;
import com.inlaco.crewmgrservice.feature.course.model.dto.CourseEnrollment;
import com.inlaco.crewmgrservice.feature.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {

  Page<Course> getCourses(Pageable pageable);

  Page<Course> getNonExpiredCourses(Pageable pageable);

  Page<CourseEnrollment> getEnrolledCourses(User user, Pageable pageable);

  CourseDetail getCourseDetailById(String id, User user);

  Course createCourse(Course newCourse);

  Course updateCourse(String id, JsonNode updatedPatch);

  void deleteCourse(String id);

  void updateEmployeeCompletionProgress(String sailorId);

  Page<Course> searchCourseByName(String keyword, Pageable pageable);
}
