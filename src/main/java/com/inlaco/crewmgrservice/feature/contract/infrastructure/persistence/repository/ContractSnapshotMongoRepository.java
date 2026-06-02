package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.repository;

import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity.ContractSnapshotEntity;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractSnapshotMongoRepository
    extends MongoRepository<ContractSnapshotEntity, String> {

  List<ContractSnapshotEntity> findByContractId(ObjectId contractId);

  List<ContractSnapshotEntity> findByContractIdAndVersionNumLessThan(
      ObjectId contractId, int version);

  Optional<ContractSnapshotEntity> findByContractIdAndVersionNum(ObjectId contractId, int version);
}
