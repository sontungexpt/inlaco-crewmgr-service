package com.inlaco.crewmgrservice.feature.course.presentation.mapper;

import com.inlaco.crewmgrservice.feature.course.domain.model.Course;
import com.inlaco.crewmgrservice.feature.course.domain.model.CourseMember;
import com.inlaco.crewmgrservice.feature.course.domain.model.CourseUpdateCommand;
import com.inlaco.crewmgrservice.feature.course.domain.model.UserCourse;
import com.inlaco.crewmgrservice.feature.course.presentation.dto.request.CoursePatchRequest;
import com.inlaco.crewmgrservice.feature.course.presentation.dto.request.NewCourseRequest;
import com.inlaco.crewmgrservice.feature.course.presentation.dto.response.CourseMemberInfoResponse;
import com.inlaco.crewmgrservice.feature.course.presentation.dto.response.CourseResponse;
import com.inlaco.crewmgrservice.feature.course.presentation.dto.response.UserCourseResponse;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadDispatcher;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.shared.application.model.Patch;
import com.inlaco.crewmgrservice.shared.mapstruct.mapper.AssetResponseMapper;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
    componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {AssetResponseMapper.class})
public abstract class CourseMapper {

  @Autowired protected UploadDispatcher uploadDispatcher;

  @Mapping(target = "wallpaper", source = "wallpaper", qualifiedByName = "mapWallpaper")
  @Mapping(
      target = "trainingProviderLogo",
      source = "trainingProviderLogo",
      qualifiedByName = "mapTrainingProviderLogo")
  public abstract Course toCourse(NewCourseRequest request);

  public abstract CourseResponse toCourseResponse(Course course);

  public abstract UserCourseResponse toUserCourseResponse(UserCourse course);

  public abstract CourseMemberInfoResponse toCourseMemberInfoResponse(CourseMember course);

  @Mapping(target = "wallpaper", source = "wallpaper", qualifiedByName = "toWallpaperPatch")
  @Mapping(
      target = "trainingProviderLogo",
      source = "trainingProviderLogo",
      qualifiedByName = "toTrainingProviderLogoPatch")
  public abstract CourseUpdateCommand toCourseUpdateCommand(CoursePatchRequest patchRequest);

  @Named("toWallpaperPatch")
  protected Patch<Asset> toWallpaperPatch(Patch<String> patch) {
    return patch.map(assetId -> uploadDispatcher.fetch(AssetType.COURSE_WALLPAPER, assetId));
  }

  @Named("toTrainingProviderLogoPatch")
  protected Patch<Asset> toTrainingProviderLogoPatch(Patch<String> patch) {
    return patch.map(assetId -> uploadDispatcher.fetch(AssetType.TRAINING_PROVIDER_LOGO, assetId));
  }

  @Named("mapWallpaper")
  protected Asset mapWallpaper(String wallpaperId) {
    return uploadDispatcher.fetch(AssetType.COURSE_WALLPAPER, wallpaperId);
  }

  @Named("mapTrainingProviderLogo")
  protected Asset mapTrainingProviderLogo(String logoId) {
    return uploadDispatcher.fetch(AssetType.TRAINING_PROVIDER_LOGO, logoId);
  }
}
