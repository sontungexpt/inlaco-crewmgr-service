package com.inlaco.crewmgrservice.feature.course.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.annotation.CurrentUser;
import com.inlaco.crewmgrservice.annotation.PageableQueryParams;
import com.inlaco.crewmgrservice.config.OpenApiConfig;
import com.inlaco.crewmgrservice.feature.course.model.Course;
import com.inlaco.crewmgrservice.feature.course.model.dto.CourseDetail;
import com.inlaco.crewmgrservice.feature.course.model.dto.CourseEnrollment;
import com.inlaco.crewmgrservice.feature.course.service.CourseService;
import com.inlaco.crewmgrservice.feature.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/courses")
@Tag(name = "Course", description = "APIs for managing courses")
public class CourseController {

  private final CourseService courseService;

  @Operation(
      summary = "Get all courses in the system",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
Get all courses in the system

If `nonExpired` is set to `true`, only non-expired courses will be returned.

**Usecase**:
- UC_crew-xem-khoa-dao-tao (View course list)

""")
  @GetMapping("")
  @PageableQueryParams
  public Page<Course> getAllCourses(
      @RequestParam(defaultValue = "true") boolean nonExpired,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    if (nonExpired) return courseService.getNonExpiredCourses(pageable);
    return courseService.getCourses(pageable);
  }

  @Operation(
      summary = "Get all courses enrolled by a user",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
Get all courses enrolled by a user

**Usecase**:
- UC_crew-xem-khoa-dao-tao (View course list)

""")
  @GetMapping("/enrolled")
  @PageableQueryParams
  public Page<CourseEnrollment> getAllCourses(
      @CurrentUser User user, @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return courseService.getEnrolledCourses(user, pageable);
  }

  @Operation(
      summary = "Get a course detail",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
Get a course detail

**Usecase**:

- UC_crew-xem-chi-tiet-khoa-dao-tao

""")
  @GetMapping("/{id}")
  public CourseDetail getCourseDetail(@CurrentUser User user, @PathVariable("id") String id) {
    return courseService.getCourseDetailById(id, user);
  }

  @Operation(
      summary = "Create a new course",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
Create a new course

**Usecase**:
- UC_admin-dong-mo-dang-ky-khoa-dao-tao

""")
  @PostMapping("")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void createNewCourse(@RequestBody Course newCourse) {
    courseService.createCourse(newCourse);
  }

  @Operation(
      summary = "Force cancel a course",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
Force cancel a course

**Usecase**:
- UC_admin-ket-thuc-som-khoa-dao-tao

""")
  @PostMapping("/force-cancel/id")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void forceCancelCourse(@PathVariable("id") String id) {
    courseService.cancelCourse(id);
  }

  @Operation(
      summary = "Update a course",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
Update a course

**Usecase**:

- UC_admin-cap-nhat-thong-tin-khoa-dao-tao

""")
  @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
  @RolesAllowed("ADMIN")
  public Course updateCourse(@PathVariable("id") String id, @RequestBody JsonNode patch) {
    return courseService.updateCourse(id, patch);
  }

  @Operation(
      summary = "Delete a course",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """

Delete a course

**Usecase**:

- UC_admin-xoa-khoa-dao-tao

""")
  @PatchMapping("/{id}")
  @RolesAllowed("ADMIN")
  public void deleteCourse(@PathVariable("id") String id) {
    courseService.deleteCourse(id);
  }

  @Operation(
      summary = "Cancel registration for a course",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
Cancel registration for a course

**Usecase**:

- UC_admin-dong-mo-dang-ky-khoa-dao-tao

""")
  @PostMapping("/cancellation/{id}")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void cancelRegistration(@PathVariable("id") String id) {
    courseService.cancelRegistrationOfCourse(id);
  }

  @Operation(
      summary = "Register for a course as a Sailor",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      responses = {
        @ApiResponse(responseCode = "204", description = "Registratoin successfully"),
        @ApiResponse(responseCode = "404", description = "Course is not found"),
        @ApiResponse(responseCode = "403", description = "Registration is closed")
      },
      description =
          """
Register for a course as a Sailor

**Usecase**:

- UC_crew-dang-ky-tham-gia-khoa-dao-tao

""")
  @PostMapping("/registration/{id}")
  @RolesAllowed("SAILOR")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void enrollCourse(@CurrentUser User user, @PathVariable("id") String id) {
    courseService.enrollCourse(id, user);
  }
}
