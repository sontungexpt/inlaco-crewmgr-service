package com.inlaco.crewmgrservice.feature.attendance.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.attendance.domain.model.Attendance;
import com.inlaco.crewmgrservice.feature.attendance.infrastructure.persistence.mongodb.entity.AttendanceEntity;
import com.inlaco.crewmgrservice.shared.mapstruct.config.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    config = CentralMapperConfig.class,
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttendanceEntityMapper {

  AttendanceEntity toAttendanceEntity(Attendance attendance);

  Attendance toAttendance(AttendanceEntity attendanceEntity);

  void updateFromAttendance(Attendance attendance, @MappingTarget AttendanceEntity existing);
}
