package com.inlaco.crewmgrservice.feature.course.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.application.exception.ResourceAlreadyInUseException;
import com.inlaco.crewmgrservice.application.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.course.exception.RegistrationClosedException;
import com.inlaco.crewmgrservice.feature.course.model.Course;
import com.inlaco.crewmgrservice.feature.course.model.CourseMember;
import com.inlaco.crewmgrservice.feature.course.model.dto.CourseDetail;
import com.inlaco.crewmgrservice.feature.course.model.dto.CourseEnrollment;
import com.inlaco.crewmgrservice.feature.course.model.dto.CourseFilterable;
import com.inlaco.crewmgrservice.feature.course.model.dto.CourseMemberInfo;
import com.inlaco.crewmgrservice.feature.course.repository.CourseMemberRepository;
import com.inlaco.crewmgrservice.feature.course.repository.CourseRepository;
import com.inlaco.crewmgrservice.feature.course.repository.CustomCourseRepository;
import com.inlaco.crewmgrservice.feature.course.service.CourseService;
import com.inlaco.crewmgrservice.feature.upload.enums.UploadStrategy;
import com.inlaco.crewmgrservice.feature.upload.service.UploadFactory;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.utils.JsonMergePatchUtils;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
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
  private final UploadFactory uploadFactory;

  @Override
  public Page<Course> getCourses(CourseFilterable courseFilterable, Pageable pageable) {
    return customCourseRepository.getCourses(courseFilterable, pageable);
  }

  @Override
  public CourseDetail getCourseDetailById(String id, User user) {
    try {
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
                log.debug(
                    "Fetching course courseMember with courseId: {} and userId: {}",
                    id,
                    user.getId());
                return courseMemberRepository
                    .findByCourseIdAndUserId(new ObjectId(id), new ObjectId(user.getId()))
                    .orElse(null);
              });

      return courseFuture
          .thenCombine(
              courseMemberFuture, (course, courseMember) -> CourseDetail.from(course, courseMember))
          .join();

    } catch (CompletionException e) {
      throw e.getCause() instanceof RuntimeException
          ? (RuntimeException) e.getCause()
          : new RuntimeException("Failed to get course detail", e.getCause());
    }
  }

  @Override
  public Course createCourse(Course newCourse, String wallpaperAssetId, String logoAssetId) {
    newCourse.setWallpaper(
        uploadFactory.metadata(UploadStrategy.COURSE_WALLPAPER, wallpaperAssetId));
    newCourse.setTrainingProviderLogo(
        uploadFactory.metadata(UploadStrategy.TRAINING_PROVIDER_LOGO, logoAssetId));

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
  }

  @Override
  public Page<CourseMemberInfo> getCourseMembers(String courseId, Pageable pageable) {
    return customCourseRepository.findEnrolledCourseSailors(courseId, pageable);
  }
}
