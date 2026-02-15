package com.inlaco.crewmgrservice.feature.course.application.service;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.course.application.model.CourseSearchCriteria;
import com.inlaco.crewmgrservice.feature.course.application.port.in.CourseUseCase;
import com.inlaco.crewmgrservice.feature.course.application.port.out.CourseMemberRepository;
import com.inlaco.crewmgrservice.feature.course.application.port.out.CourseRepository;
import com.inlaco.crewmgrservice.feature.course.domain.exception.RegistrationClosedException;
import com.inlaco.crewmgrservice.feature.course.domain.model.Course;
import com.inlaco.crewmgrservice.feature.course.domain.model.CourseMember;
import com.inlaco.crewmgrservice.feature.course.domain.model.UserCourse;
import com.inlaco.crewmgrservice.feature.upload.application.enums.UploadStrategy;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadFactory;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceAlreadyInUseException;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.shared.support.JsonMergePatchUtils;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseService implements CourseUseCase {

  private final JsonMergePatchUtils jsonMergePatchUtils;
  private final UploadFactory uploadFactory;
  private final CourseRepository courseRepository;
  private final CourseMemberRepository courseMemberRepository;

  @Override
  public void deleteCourse(String id) {
    courseRepository.deleteById(id);
  }

  @Override
  public Page<Course> getCourses(CourseSearchCriteria criteria, Pageable pageable) {
    return courseRepository.findAll(criteria, pageable);
  }

  @Override
  public Course getCourse(String id) {
    return courseRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(Course.class, "id", id));
  }

  @Override
  public UserCourse getUserCourse(String id, User user) {
    return supplyAsync(
            () -> {
              log.debug("Fetching course with id: {}", id);
              return courseRepository
                  .findById(id)
                  .orElseThrow(() -> new ResourceNotFoundException(Course.class, "id", id));
            })
        .thenCombine(
            supplyAsync(
                () -> {
                  log.debug(
                      "Fetching course courseMember with courseId: {} and userId: {}",
                      id,
                      user.getId());
                  return courseMemberRepository
                      .findByCourseIdAndUserId(id, user.getId())
                      .orElse(null);
                }),
            (course, courseMember) -> {
              var builder = UserCourse.builder().course(course);
              if (courseMember != null) {
                builder
                    .certificate(courseMember.getCertificate())
                    .cancelledAt(courseMember.getCancelledAt())
                    .status(courseMember.getStatus())
                    .completionProgress(courseMember.getCompletionProgress())
                    .note(courseMember.getNote())
                    .enrolledAt(courseMember.getCreatedAt());
              }
              return builder.build();
            })
        .join();
  }

  @Override
  public Course createCourse(Course newCourse, String wallpaperAssetId, String logoAssetId) {
    newCourse.setWallpaper(
        uploadFactory.metadata(UploadStrategy.COURSE_WALLPAPER, wallpaperAssetId));
    newCourse.setTrainingProviderLogo(
        uploadFactory.metadata(UploadStrategy.TRAINING_PROVIDER_LOGO, logoAssetId));

    return courseRepository.save(newCourse);
  }

  @Override
  public Course updateCourse(String id, JsonNode updatedPatch) {
    return jsonMergePatchUtils.patch(id, Course.class, updatedPatch);
  }

  @Override
  public void updateEmployeeCompletionProgress(String sailorId) {
    throw new UnsupportedOperationException(
        "Unimplemented method 'updateEmployeeCompletionProgress'");
  }

  @Override
  public Page<UserCourse> getEnrolledUserCourses(User user, Pageable pageable) {
    return courseRepository.findAllEnrolled(user.getId(), pageable);
  }

  @Override
  @Transactional
  public void cancelCourse(String id) {
    Course course = getCourse(id);
    course.forceCancel();
    courseRepository.save(course);
    List<CourseMember> courseMembers = courseMemberRepository.findByCourseId(course.getId());
    courseMembers.forEach(CourseMember::forceFinished);
    courseMemberRepository.saveAll(courseMembers);
  }

  @Override
  public void cancelCourseRegistration(String id) {
    Course course = getCourse(id);
    course.manuallyDisableRegistration();
    courseRepository.save(course);
  }

  @Override
  @Transactional
  public void enrollCourse(String courseId, User user) {
    Course course = getCourse(courseId);

    if (!course.isRegistrationEnabled()) {
      throw new RegistrationClosedException("Registration is closed for this course");
    } else if (courseMemberRepository.existsByCourseIdAndUserId(courseId, user.getId())) {
      throw new ResourceAlreadyInUseException(
          CourseMember.class, Map.of("courseId", courseId, "userId", user.getId()));
    }
    courseMemberRepository.save(new CourseMember(courseId, user.getId()));

    course.increaseEnrolledStudentCount();
    courseRepository.save(course);
  }

  @Override
  public void markCourseCompleted(String courseId, String userId) {
    CourseMember courseMember =
        courseMemberRepository
            .findByCourseIdAndUserId(courseId, userId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        CourseMember.class,
                        Map.of(
                            "courseId", courseId,
                            "userId", userId)));

    courseMember.complete();
    courseMemberRepository.save(courseMember);
  }

  @Override
  public Page<CourseMember> getCourseMembers(String courseId, Pageable pageable) {
    throw new UnsupportedOperationException("Unimplemented method 'getCourseMembers'");
    // return customCourseRepository.findEnrolledCourseSailors(courseId, pageable);
  }
}
