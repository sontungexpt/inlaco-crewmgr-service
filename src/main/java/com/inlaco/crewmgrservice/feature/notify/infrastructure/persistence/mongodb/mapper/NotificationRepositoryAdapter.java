package com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.notify.application.port.out.NotificationRepository;
import com.inlaco.crewmgrservice.feature.notify.domain.model.Notification;
import com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.adapter.NotificationEntityMapper;
import com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.entity.NotificationEntity;
import com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.repository.NotificationMongoRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepository {

  private final NotificationMongoRepository repository;
  private final NotificationEntityMapper mapper;
  private final MongoTemplate mongoTempalte;

  @Override
  public Notification save(Notification notification) {
    String id = notification.getId();
    if (id == null) {
      return mapper.toNotification(repository.insert(mapper.toNotificationEntity(notification)));
    } else {
      NotificationEntity entity =
          repository
              .findById(id)
              .map(
                  existing -> {
                    mapper.updateFromNotification(notification, existing);
                    return existing;
                  })
              .orElseGet(() -> mapper.toNotificationEntity(notification));
      return mapper.toNotification(repository.save(entity));
    }
  }

  @Override
  public long countUnread(String recipientId) {
    Query query =
        new Query()
            .addCriteria(
                Criteria.where("recipientId").is(new ObjectId(recipientId)).and("read").is(false));
    return mongoTempalte.count(query, NotificationEntity.class);
  }

  @Override
  public void markAsRead(String notificationId, String recipientId) {
    Query query =
        new Query(
            Criteria.where("_id")
                .is(notificationId)
                .and("recipientId")
                .is(new ObjectId(recipientId))
                .and("read")
                .is(false));

    Update update = new Update().set("read", true);
    mongoTempalte.updateFirst(query, update, NotificationEntity.class);
  }

  @Override
  public void markAllAsRead(String recipientId) {
    Query query =
        new Query(
            Criteria.where("recipientId").is(new ObjectId(recipientId)).and("read").is(false));

    Update update = new Update().set("read", true);

    mongoTempalte.updateMulti(query, update, NotificationEntity.class);
  }

  @Override
  public Page<Notification> findByRecipientId(String recipientId, Pageable pageable) {
    return repository
        .findByRecipientId(new ObjectId(recipientId), pageable)
        .map(mapper::toNotification);
  }
}
