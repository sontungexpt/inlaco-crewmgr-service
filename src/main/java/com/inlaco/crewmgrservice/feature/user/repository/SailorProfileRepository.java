package com.inlaco.crewmgrservice.feature.user.repository;

import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SailorProfileRepository extends MongoRepository<SailorProfile, String> {

  Optional<SailorProfile> findByAccountId(String accountId);

  Optional<SailorProfile> findByCardId(String cardId);

  Optional<SailorProfile> findByContractId(String contractId);
}
