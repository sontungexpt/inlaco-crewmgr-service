package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceLog;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.AttendanceLogEntity;
import com.inlaco.crewmgrservice.shared.mapstruct.config.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    config = CentralMapperConfig.class,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AttendanceLogEntityMapper {

  AttendanceLogEntity toEntity(AttendanceLog domain);

  AttendanceLog toDomain(AttendanceLogEntity entity);
}
