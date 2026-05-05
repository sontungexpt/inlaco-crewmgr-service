package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.QRCodeRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.QRCode;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.QRCodeEntity;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.repository.QRCodeMongoRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QRCodeRepositoryAdapter implements QRCodeRepository {
  
  private final QRCodeMongoRepository mongoRepository;
  
  @Override
  public QRCode save(QRCode qrCode) {
    QRCodeEntity entity = QRCodeEntity.fromDomain(qrCode);
    QRCodeEntity saved = mongoRepository.save(entity);
    return saved.toDomain();
  }
  
  @Override
  public Optional<QRCode> findByToken(String token) {
    return mongoRepository.findByToken(token)
        .map(QRCodeEntity::toDomain);
  }
  
  @Override
  public Optional<QRCode> findByTokenAndNotUsed(String token) {
    return mongoRepository.findByTokenAndNotUsed(token)
        .map(QRCodeEntity::toDomain);
  }
  
  @Override
  public void deleteById(String id) {
    mongoRepository.deleteById(id);
  }
}
