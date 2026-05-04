package com.inlaco.crewmgrservice.feature.attendance.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.attendance.domain.model.AttendanceLog;
import com.inlaco.crewmgrservice.feature.attendance.infrastructure.persistence.mongodb.entity.AttendanceLogEntity;
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

  AttendanceLogEntity toAttendanceEntity(AttendanceLog log);

  AttendanceLog toAttendance(AttendanceLogEntity entity);

  void updateFromAttendance(AttendanceLog log, @MappingTarget AttendanceLogEntity existing);
}
