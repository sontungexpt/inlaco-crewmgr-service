package com.inlaco.crewmgrservice.feature.crew.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.crew.infrastructure.persistence.mongodb.entity.CrewProfileEntity;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewProfileMongoRepository extends MongoRepository<CrewProfileEntity, String> {

  Optional<CrewProfileEntity> findByAccountId(ObjectId accountId);

  Optional<CrewProfileEntity> findByEmployeeCardId(String cardId);

  List<CrewProfileEntity> findByIdIn(Iterable<String> ids);

  List<CrewProfileEntity> findByEmployeeCardIdIn(Iterable<String> cardIds);
}
