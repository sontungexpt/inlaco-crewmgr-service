package com.inlaco.crewmgrservice.feature.course.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.course.application.model.CourseSearchCriteria;
import com.inlaco.crewmgrservice.feature.course.application.port.in.CourseUseCase;
import com.inlaco.crewmgrservice.feature.course.domain.model.Course;
import com.inlaco.crewmgrservice.feature.course.presentation.dto.request.CoursePatchRequest;
import com.inlaco.crewmgrservice.feature.course.presentation.dto.request.NewCourseRequest;
import com.inlaco.crewmgrservice.feature.course.presentation.dto.response.CourseMemberInfoResponse;
import com.inlaco.crewmgrservice.feature.course.presentation.dto.response.CourseResponse;
import com.inlaco.crewmgrservice.feature.course.presentation.dto.response.UserCourseResponse;
import com.inlaco.crewmgrservice.feature.course.presentation.mapper.CourseMapper;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.Filter;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.PageableQueryParams;
import com.inlaco.crewmgrservice.infrastructure.web.validation.annotation.ObjectId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/courses")
@Tag(name = "Course", description = "APIs for managing courses")
public class CourseController {

  private final CourseUseCase courseUseCase;
  private final CourseMapper courseMapper;

  @Operation(
      summary = "Get all courses in the system",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("")
  @PageableQueryParams
  public Page<CourseResponse> getAllCourses(
      @Filter CourseSearchCriteria criteria,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return courseUseCase.getCourses(criteria, pageable).map(courseMapper::toCourseResponse);
  }

  @Operation(
      summary = "Get a course detail",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/{id}")
  @RolesAllowed("SAILOR")
  public UserCourseResponse getUserCourse(
      @CurrentUser User user, @ObjectId @PathVariable("id") String id) {
    return courseMapper.toUserCourseResponse(courseUseCase.getUserCourse(id, user));
  }

  @Operation(
      summary = "Create a new course",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.CREATED)
  public CourseResponse createNewCourse(@Valid @RequestBody NewCourseRequest newCourse) {
    return courseMapper.toCourseResponse(
        courseUseCase.createCourse(courseMapper.toCourse(newCourse)));
  }

  @Operation(
      summary = "Force cancel a course",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("/force-cancel/{id}")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void forceCancelCourse(@ObjectId @PathVariable("id") String id) {
    courseUseCase.cancelCourse(id);
  }

  @Operation(
      summary = "Update a course by id",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed("ADMIN")
  @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
  public Course updateCourse(
      @PathVariable("id") @ObjectId String id, @RequestBody CoursePatchRequest patch) {
    return courseUseCase.updateCourse(id, courseMapper.toCourseUpdateCommand(patch));
  }

  @Operation(
      summary = "Get all sailors enrolled in a course",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed("ADMIN")
  @GetMapping("/{courseId}/members")
  @PageableQueryParams
  public List<CourseMemberInfoResponse> getCourseMembers(
      @ObjectId @PathVariable("courseId") String courseId,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return courseUseCase.getCourseMembers(courseId, pageable).stream()
        .map(courseMapper::toCourseMemberInfoResponse)
        .collect(Collectors.toList());
  }

  @Operation(
      summary = "Delete a course",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PatchMapping("/{id}")
  @RolesAllowed("ADMIN")
  public void deleteCourse(@PathVariable("id") @ObjectId String id) {
    courseUseCase.deleteCourse(id);
  }

  @Operation(
      summary = "Cancel registration for a course",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("/registration/cancellation/{id}")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void cancelRegistration(@PathVariable("id") @ObjectId String id) {
    courseUseCase.cancelCourseRegistration(id);
  }

  @Operation(
      summary = "Register for a course as a Sailor",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("/registration/{id}")
  @RolesAllowed("SAILOR")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void enrollCourse(@CurrentUser User user, @ObjectId @PathVariable("id") String id) {
    courseUseCase.enrollCourse(id, user);
  }

  @Operation(
      summary = "Mark completation for a sailor",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("/{courseId}/completation/{userId}")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void markCourseCompleted(
      @ObjectId @PathVariable("courseId") String courseId,
      @ObjectId @PathVariable("userId") String userId) {
    courseUseCase.markCourseCompleted(courseId, userId);
  }
}
