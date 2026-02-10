package com.inlaco.crewmgrservice.feature.course.presentation.mapper;

import com.inlaco.crewmgrservice.feature.course.domain.model.Course;
import com.inlaco.crewmgrservice.feature.course.domain.model.CourseMember;
import com.inlaco.crewmgrservice.feature.course.domain.model.UserCourse;
import com.inlaco.crewmgrservice.feature.course.presentation.dto.request.NewCourseRequest;
import com.inlaco.crewmgrservice.feature.course.presentation.dto.response.CourseMemberInfoResponse;
import com.inlaco.crewmgrservice.feature.course.presentation.dto.response.CourseResponse;
import com.inlaco.crewmgrservice.feature.course.presentation.dto.response.UserCourseResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {

  Course toCourse(CourseResponse dto);

  Course toCourse(NewCourseRequest request);

  CourseResponse toCourseDTO(Course course);

  UserCourseResponse toUserCourseDTO(UserCourse course);

  CourseMemberInfoResponse toCourseMemberInfoDTO(CourseMember course);
}
