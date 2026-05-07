package com.inlaco.crewmgrservice.feature.otp.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.otp.domain.model.Otp;
import com.inlaco.crewmgrservice.feature.otp.infrastructure.persistence.mongodb.entity.OtpEntity;
import com.inlaco.crewmgrservice.shared.mapstruct.config.CentralMapperConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CentralMapperConfig.class)
public interface OtpEntityMapper {

  Otp toOtp(OtpEntity entity);

  OtpEntity toOtpEntity(Otp otp);
}
