package com.inlaco.crewmgrservice.feature.course.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.annotation.CurrentUser;
import com.inlaco.crewmgrservice.annotation.PageableQueryParams;
import com.inlaco.crewmgrservice.config.OpenApiConfig;
import com.inlaco.crewmgrservice.feature.course.model.Course;
import com.inlaco.crewmgrservice.feature.course.model.dto.CourseDetail;
import com.inlaco.crewmgrservice.feature.course.service.CourseService;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.validation.annotation.ObjectId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
      summary = "Search course by keyword in the name or archived position",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
Search course by keyword in the name or archived position

If `nonExpired` is set to `true`, only non-expired courses will be returned.

**Usecase**:
- UC_crew-tim-kiem-khoa-dao-tao.

""")
  @GetMapping("/search")
  @PageableQueryParams
  @RolesAllowed("SAILOR")
  public Page<Course> searchCourses(
      @RequestParam String q,
      @RequestParam(defaultValue = "true") boolean nonExpired,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return courseService.searchCourses(q, nonExpired, pageable);
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
  public CourseDetail getCourseDetail(
      @CurrentUser User user, @ObjectId @PathVariable("id") String id) {
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
  @ResponseStatus(HttpStatus.CREATED)
  public Course createNewCourse(@Valid @RequestBody Course newCourse) {
    return courseService.createCourse(newCourse);
  }

  @Operation(
      summary = "Force cancel a course",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
Force cancel a course by id

**Usecase**:
- UC_admin-ket-thuc-som-khoa-dao-tao

""")
  @PostMapping("/force-cancel/{id}")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void forceCancelCourse(@ObjectId @PathVariable("id") String id) {
    courseService.cancelCourse(id);
  }

  @Operation(
      summary = "Update a course",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
Update a course by id

**Usecase**:

- UC_admin-cap-nhat-thong-tin-khoa-dao-tao

""")
  @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
  @RolesAllowed("ADMIN")
  public Course updateCourse(@PathVariable("id") @ObjectId String id, @RequestBody JsonNode patch) {
    return courseService.updateCourse(id, patch);
  }

  @Operation(
      summary = "Get all sailors enrolled in a course",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
Get all sailors enrolled in a course

**Usecase**:

- UC_admin-xem-danh-sach-cac-thuyen-vien-dang-hoc.

**NOTE**:
- This API is only accessible by the admin.
- The response is paginated.
- The default page size is 20.

""")
  @RolesAllowed("ADMIN")
  @GetMapping("/{courseId}/members")
  @PageableQueryParams
  public ResponseEntity<?> getCourseMembers(
      @ObjectId @PathVariable("courseId") String courseId,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return ResponseEntity.ok(courseService.getCourseMembers(courseId, pageable));
  }

  @Operation(
      summary = "Delete a course",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """

Delete a course by id

**Usecase**:

- UC_admin-xoa-khoa-dao-tao

""")
  @PatchMapping("/{id}")
  @RolesAllowed("ADMIN")
  public void deleteCourse(@PathVariable("id") @ObjectId String id) {
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
  public void cancelRegistration(@PathVariable("id") @ObjectId String id) {
    courseService.cancelCourseRegistration(id);
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
  public void enrollCourse(@CurrentUser User user, @ObjectId @PathVariable("id") String id) {
    courseService.enrollCourse(id, user);
  }

  @Operation(
      summary = "Mark completation for a sailor",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
Mark completation for a sailor

**Usecase**:
- UC_admin-danh-dau-thuyen-vien-hoan-thanh-khoa-hoc.

""")
  @PostMapping("/{courseId}/completation/{userId}")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void markCourseCompletation(
      @ObjectId @PathVariable("courseId") String courseId,
      @ObjectId @PathVariable("userId") String userId) {
    courseService.markSailorCompletedCourse(courseId, userId);
  }
}
