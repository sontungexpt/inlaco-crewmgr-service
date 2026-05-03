package com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.mapper;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.feature.notify.application.port.out.NotificationRepository;
import com.inlaco.crewmgrservice.feature.notify.domain.model.Notification;
import com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.adapter.NotificationEntityMapper;
import com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.entity.NotificationEntity;
import com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.repository.NotificationMongoRepository;
import com.inlaco.crewmgrservice.infrastructure.persistence.mongodb.aggregation.FacetResult;
import com.inlaco.crewmgrservice.infrastructure.persistence.support.PageableUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class NotificationRepositoryAdapter implements NotificationRepository {

  private final NotificationMongoRepository repository;
  private final NotificationEntityMapper mapper;
  private final MongoTemplate mongoTempalte;

  static class NotificationFacetResult extends FacetResult<NotificationEntity> {}

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
    pageable = PageableUtils.prependSort(pageable, Order.asc("read"));
    pageable = PageableUtils.enforceIdSort(pageable, NotificationEntity.class);

    Criteria criteria = Criteria.where("recipientId").is(new ObjectId(recipientId));

    Aggregation aggregation =
        newAggregation(
            match(criteria),
            facet(Aggregation.count().as(FacetResult.COUNT_KEY))
                .as(FacetResult.COUNT_FACET_NAME)
                .and(
                    sort(pageable.getSort()),
                    skip(pageable.getOffset()),
                    limit(pageable.getPageSize()))
                .as(FacetResult.DATA_FACET_NAME));

    return mongoTempalte
        .aggregate(aggregation, NotificationEntity.class, NotificationFacetResult.class)
        .getUniqueMappedResult()
        .toPage(pageable)
        .map(mapper::toNotification);
  }

  @Override
  public List<Notification> saveAll(Iterable<Notification> notifications) {
    if (notifications == null) return Collections.emptyList();
    Streamable<Notification> source = Streamable.of(notifications);
    if (source.isEmpty()) return Collections.emptyList();

    List<NotificationEntity> newEntities = new ArrayList<>();
    List<Notification> updateNotifications = new ArrayList<>();
    source.stream()
        .forEach(
            notification -> {
              String id = notification.getId();
              if (id == null) {
                newEntities.add(mapper.toNotificationEntity(notification));
              } else {
                updateNotifications.add(notification);
              }
            });

    if (updateNotifications.isEmpty()) {
      return mongoTempalte.insert(newEntities, NotificationEntity.class).stream()
          .map(mapper::toNotification)
          .toList();
    }

    List<String> resultIds =
        updateNotifications.stream().map(Notification::getId).collect(Collectors.toList());

    Map<String, NotificationEntity> existingMap =
        repository.findAllById(resultIds).stream()
            .collect(Collectors.toMap(NotificationEntity::getId, Function.identity()));

    BulkOperations bulkOps = mongoTempalte.bulkOps(BulkMode.UNORDERED, NotificationEntity.class);
    if (!newEntities.isEmpty()) {
      bulkOps.insert(newEntities);
    }

    for (Notification notification : updateNotifications) {
      String id = notification.getId();
      NotificationEntity existing = existingMap.get(id);
      if (existing == null) {
        bulkOps.insert(mapper.toNotificationEntity(notification));
      } else {
        mapper.updateFromNotification(notification, existing);
        bulkOps.replaceOne(Query.query(Criteria.where("_id").is(id)), existing);
      }
    }
    bulkOps
        .execute()
        .getInserts()
        .forEach(r -> resultIds.add(r.getId().asObjectId().getValue().toHexString()));

    return repository.findAllById(resultIds).stream().map(mapper::toNotification).toList();
  }

  @Override
  public Optional<Notification> findByIdAndRecipientId(String notificationId, String recipientId) {
    return repository
        .findByIdAndRecipientId(notificationId, new ObjectId(recipientId))
        .map(mapper::toNotification);
  }
}
