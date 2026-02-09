package com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.redis.mapper;

import com.inlaco.crewmgrservice.feature.auth.domain.model.EmailVerificationToken;
import com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.redis.entity.EmailVerificationTokenEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EmailVerificationTokenRedisMapper {

  EmailVerificationToken toDomain(EmailVerificationTokenEntity tokenEntity);

  EmailVerificationTokenEntity toEntity(EmailVerificationToken token);
}
