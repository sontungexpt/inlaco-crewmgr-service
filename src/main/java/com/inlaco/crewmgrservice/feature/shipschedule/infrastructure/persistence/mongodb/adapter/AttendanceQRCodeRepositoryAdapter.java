package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.AttendanceQRCodeRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceQRCode;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.AttendanceQRCodeEntity;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.repository.AttendanceQRCodeMongoRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AttendanceQRCodeRepositoryAdapter implements AttendanceQRCodeRepository {

  private final AttendanceQRCodeMongoRepository mongoRepository;

  @Override
  public AttendanceQRCode save(AttendanceQRCode qrCode) {
    AttendanceQRCodeEntity entity = AttendanceQRCodeEntity.fromDomain(qrCode);
    AttendanceQRCodeEntity saved = mongoRepository.save(entity);
    return saved.toDomain();
  }

  @Override
  public Optional<AttendanceQRCode> findByToken(String token) {
    return mongoRepository.findByToken(token).map(AttendanceQRCodeEntity::toDomain);
  }

  @Override
  public Optional<AttendanceQRCode> findByTokenAndNotUsed(String token) {
    return mongoRepository.findByTokenAndNotUsed(token).map(AttendanceQRCodeEntity::toDomain);
  }

  @Override
  public void deleteById(String id) {
    mongoRepository.deleteById(id);
  }
}
