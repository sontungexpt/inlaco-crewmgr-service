package com.inlaco.crewmgrservice.feature.course.presentation.mapper;

import com.inlaco.crewmgrservice.feature.course.domain.model.Course;
import com.inlaco.crewmgrservice.feature.course.domain.model.CourseMember;
import com.inlaco.crewmgrservice.feature.course.domain.model.UserCourse;
import com.inlaco.crewmgrservice.feature.course.presentation.dto.request.NewCourseRequest;
import com.inlaco.crewmgrservice.feature.course.presentation.dto.response.CourseMemberInfoResponse;
import com.inlaco.crewmgrservice.feature.course.presentation.dto.response.CourseResponse;
import com.inlaco.crewmgrservice.feature.course.presentation.dto.response.UserCourseResponse;
import com.inlaco.crewmgrservice.shared.mapstruct.mapper.AssetResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = AssetResponseMapper.class)
public interface CourseMapper {

  @Mapping(target = "wallpaper", ignore = true)
  @Mapping(target = "trainingProviderLogo", ignore = true)
  Course toCourse(CourseResponse dto);

  Course toCourse(NewCourseRequest request);

  CourseResponse toCourseDTO(Course course);

  UserCourseResponse toUserCourseDTO(UserCourse course);

  CourseMemberInfoResponse toCourseMemberInfoDTO(CourseMember course);
}
