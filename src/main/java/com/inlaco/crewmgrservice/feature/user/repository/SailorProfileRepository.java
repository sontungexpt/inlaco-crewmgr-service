package com.inlaco.crewmgrservice.feature.user.repository;

import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SailorProfileRepository extends MongoRepository<SailorProfile, String> {

  boolean existsByCandidateId(ObjectId candidateId);

  Optional<SailorProfile> findByAccountId(ObjectId accountId);

  Optional<SailorProfile> findByCardId(String cardId);

  List<SailorProfile> findByCardIdIn(Iterable<String> cardIds);
}
