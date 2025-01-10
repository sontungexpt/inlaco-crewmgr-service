package com.inlaco.crewmgrservice.feature.course.controller;

import com.inlaco.crewmgrservice.annotation.PageableQueryParams;
import com.inlaco.crewmgrservice.feature.course.model.Course;
import com.inlaco.crewmgrservice.feature.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

  private final CourseService courseService;

  @GetMapping("")
  @PageableQueryParams
  public Page<Course> getAllCourses(@PageableDefault Pageable pageable) {
    return courseService.getCourses(pageable);
  }
}
