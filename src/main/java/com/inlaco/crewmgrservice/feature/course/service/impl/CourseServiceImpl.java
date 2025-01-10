package com.inlaco.crewmgrservice.feature.course.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.course.model.Course;
import com.inlaco.crewmgrservice.feature.course.model.CourseMemberTracking;
import com.inlaco.crewmgrservice.feature.course.model.dto.CourseDetail;
import com.inlaco.crewmgrservice.feature.course.model.dto.CourseEnrollment;
import com.inlaco.crewmgrservice.feature.course.repository.CourseMemberTrackingRepository;
import com.inlaco.crewmgrservice.feature.course.repository.CourseRepository;
import com.inlaco.crewmgrservice.feature.course.repository.CustomCourseRepository;
import com.inlaco.crewmgrservice.feature.course.service.CourseService;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.utils.JsonMergePatchUtils;
import com.inlaco.crewmgrservice.utils.PageableUtils;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

  private final CustomCourseRepository customCourseRepository;
  private final JsonMergePatchUtils jsonMergePatchUtils;
  private final CourseRepository courseRepository;
  private final CourseMemberTrackingRepository courseMemberTrackingRepository;

  @Override
  public Page<Course> getCourses(Pageable pageable) {
    return courseRepository.findByIsDeleted(false, PageableUtils.extendDefaultSort(pageable));
  }

  @Override
  public CourseDetail getCourseDetailById(String id, User user) {
    CompletableFuture<Course> courseFuture =
        CompletableFuture.supplyAsync(
            () -> {
              log.info("Fetching course with id: {}", id);
              return courseRepository
                  .findById(id)
                  .orElseThrow(() -> new ResourceNotFoundException(Course.class, "id", id));
            });

    CompletableFuture<CourseMemberTracking> trackingFuture =
        CompletableFuture.supplyAsync(
            () -> {
              log.info(
                  "Fetching course tracking with courseId: {} and userId: {}", id, user.getId());
              return courseMemberTrackingRepository
                  .findByCourseIdAndUserId(new ObjectId(id), new ObjectId(user.getId()))
                  .orElseThrow(
                      () ->
                          new ResourceNotFoundException(
                              CourseMemberTracking.class,
                              Map.of("courseId", id, "userId", user.getId())));
            });

    CompletableFuture.allOf(courseFuture, trackingFuture).join();

    try {
      Course course = courseFuture.get();
      CourseMemberTracking courseMemberTracking = trackingFuture.get();
      return CourseDetail.from(course, courseMemberTracking);
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException("Error fetching course details", e);
    }
  }

  @Override
  public Course createCourse(Course newCourse) {
    Course course = courseRepository.save(newCourse);
    return course;
  }

  @Override
  public Course updateCourse(String id, JsonNode updatedPatch) {
    return jsonMergePatchUtils.patch(id, Course.class, updatedPatch);
  }

  @Override
  public void deleteCourse(String id) {
    Course course =
        courseRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(Course.class, "id", id));
    course.setDeleted(true);
    courseRepository.save(course);
  }

  @Override
  public void updateEmployeeCompletionProgress(String sailorId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
        "Unimplemented method 'updateEmployeeCompletionProgress'");
  }

  @Override
  public Page<Course> searchCourseByName(String keyword, Pageable pageable) {
    return courseRepository.findByIsDeletedAndNameContainingIgnoreCase(
        false, keyword, PageableUtils.extendDefaultSort(pageable));
  }

  @Override
  public Page<CourseEnrollment> getEnrolledCourses(User user, Pageable pageable) {
    return customCourseRepository.findEnrolledCourses(user.getId(), pageable);
  }
}
