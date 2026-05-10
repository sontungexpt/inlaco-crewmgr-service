package com.inlaco.crewmgrservice.feature.shipschedule.presentation.mapper;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceLog;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceQRCode;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response.AttendanceLogResponse;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response.AttendanceQRCodeResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

  AttendanceQRCodeResponse toAttendanceQRCodeResponse(AttendanceQRCode qrCode);

  AttendanceLogResponse toAttendanceLogResponse(AttendanceLog log);
}
