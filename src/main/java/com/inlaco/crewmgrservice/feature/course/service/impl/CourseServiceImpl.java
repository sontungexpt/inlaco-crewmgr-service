package com.inlaco.crewmgrservice.feature.course.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.exceptions.ResourceAlreadyInUseException;
import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.course.exception.RegistrationClosedException;
import com.inlaco.crewmgrservice.feature.course.model.Course;
import com.inlaco.crewmgrservice.feature.course.model.CourseMember;
import com.inlaco.crewmgrservice.feature.course.model.dto.CourseDetail;
import com.inlaco.crewmgrservice.feature.course.model.dto.CourseEnrollment;
import com.inlaco.crewmgrservice.feature.course.model.dto.CourseMemberInfo;
import com.inlaco.crewmgrservice.feature.course.repository.CourseMemberRepository;
import com.inlaco.crewmgrservice.feature.course.repository.CourseRepository;
import com.inlaco.crewmgrservice.feature.course.repository.CustomCourseRepository;
import com.inlaco.crewmgrservice.feature.course.service.CourseService;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.utils.JsonMergePatchUtils;
import com.inlaco.crewmgrservice.utils.PageableUtils;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

  private final CustomCourseRepository customCourseRepository;
  private final JsonMergePatchUtils jsonMergePatchUtils;
  private final CourseRepository courseRepository;
  private final CourseMemberRepository courseMemberRepository;

  @Override
  public Page<Course> getCourses(Pageable pageable) {
    return courseRepository.findByDeleted(false, PageableUtils.extendDefaultSort(pageable));
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

    CompletableFuture<CourseMember> courseMemberFuture =
        CompletableFuture.supplyAsync(
            () -> {
              log.info(
                  "Fetching course courseMember with courseId: {} and userId: {}",
                  id,
                  user.getId());
              return courseMemberRepository
                  .findByCourseIdAndUserId(new ObjectId(id), new ObjectId(user.getId()))
                  .orElse(null);
            });

    CompletableFuture.allOf(courseFuture, courseMemberFuture).join();

    try {
      Course course = courseFuture.get();
      CourseMember courseMemberTracking = courseMemberFuture.get();
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
    courseRepository.shortDeleteById(id);
  }

  @Override
  public void updateEmployeeCompletionProgress(String sailorId) {
    throw new UnsupportedOperationException(
        "Unimplemented method 'updateEmployeeCompletionProgress'");
  }

  @Override
  public Page<Course> searchCourses(String keyword, boolean nonExpired, Pageable pageable) {
    return customCourseRepository.searchCourse(keyword, nonExpired, pageable);
  }

  @Override
  public Page<CourseEnrollment> getEnrolledCourses(User user, Pageable pageable) {
    return customCourseRepository.findCourseEnrollments(user.getId(), pageable);
  }

  @Override
  public Page<Course> getNonExpiredCourses(Pageable pageable) {
    return customCourseRepository.findByNonExpiredCourses(pageable);
  }

  @Override
  public void cancelCourse(String id) {
    Course course = getCourseById(id);

    course.forceCancel();
    List<CourseMember> courseMembers =
        courseMemberRepository.findByCourseId(new ObjectId(course.getId()));

    courseMembers.forEach(
        (it) -> {
          it.forceFinished();
        });
    courseMemberRepository.saveAll(courseMembers);
  }

  @Override
  public Course getCourseById(String id) {
    return courseRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(Course.class, "id", id));
  }

  @Override
  public void cancelCourseRegistration(String id) {
    Course course = getCourseById(id);
    course.manuallyDisableRegistration();
    courseRepository.save(course);
  }

  @Override
  @Transactional
  public void enrollCourse(String courseId, User user) {
    Course course = getCourseById(courseId);

    ObjectId userIdObj = new ObjectId(user.getId());
    ObjectId courseIdObj = new ObjectId(courseId);

    if (!course.isRegistrationEnabled()) {
      throw new RegistrationClosedException("Registration is closed for this course");
    } else if (courseMemberRepository.existsByCourseIdAndUserId(courseIdObj, userIdObj)) {
      throw new ResourceAlreadyInUseException(
          CourseMember.class, Map.of("courseId", courseId, "userId", user.getId()));
    }

    var courseMember = CourseMember.builder().courseId(courseIdObj).userId(userIdObj).build();
    courseMemberRepository.save(courseMember);

    course.increaseEnrolledStudentCount();
    courseRepository.save(course);
  }

  @Override
  public void markSailorCompletedCourse(String courseId, String userId) {
    var courseIdObj = new ObjectId(courseId);
    var userIdObj = new ObjectId(userId);
    CourseMember courseMember =
        courseMemberRepository
            .findByCourseIdAndUserId(courseIdObj, userIdObj)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        CourseMember.class,
                        Map.of(
                            "courseId", courseId,
                            "userId", userIdObj)));

    courseMember.complete();
    courseMemberRepository.save(courseMember);
    // TODO: generate certification

  }

  @Override
  public Page<CourseMemberInfo> getCourseMembers(String courseId, Pageable pageable) {
    return customCourseRepository.findEnrolledCourseSailors(courseId, pageable);
  }
}
