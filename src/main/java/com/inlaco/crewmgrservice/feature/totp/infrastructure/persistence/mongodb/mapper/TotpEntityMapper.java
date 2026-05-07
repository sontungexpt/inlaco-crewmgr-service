package com.inlaco.crewmgrservice.feature.totp.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import com.inlaco.crewmgrservice.feature.totp.infrastructure.persistence.mongodb.entity.TotpSecretEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TotpEntityMapper {

  TotpSecretEntity toTotpSecretEntity(TotpSecret domain);

  TotpSecret toTotpSecret(TotpSecretEntity entity);
}
