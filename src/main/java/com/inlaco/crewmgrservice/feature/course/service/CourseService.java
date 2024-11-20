package com.inlaco.crewmgrservice.feature.course.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.course.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {

  Page<Course> getCourses(Pageable pageable);

  Course getCourseDetailById(String id);

  Course createCourse(Course newCourse);

  Course updateCourse(String id, JsonNode updatedPatch);

  void deleteCourse(String id);

  void updateEmployeeCompletionProgress(String sailorId);

  Page<Course> searchCourseByName(String keyword, Pageable pageable);
}
