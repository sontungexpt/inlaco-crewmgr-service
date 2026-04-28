package com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.entity.NotificationEntity;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationMongoRepository extends MongoRepository<NotificationEntity, String> {

  Page<NotificationEntity> findByRecipientId(ObjectId recipientId, Pageable pageable);
}
