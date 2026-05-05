package com.inlaco.crewmgrservice.feature.shipschedule.presentation.mapper;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.QRCode;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response.QRCodeResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QRCodeMapper {
  
  QRCodeResponse toResponse(QRCode qrCode);
}
