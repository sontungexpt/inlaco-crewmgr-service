package com.inlaco.crewmgrservice.feature.shipschedule.presentation.mapper;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceQRCode;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response.AttendanceQRCodeResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttendanceQRCodeMapper {

  AttendanceQRCodeResponse toResponse(AttendanceQRCode qrCode);
}
