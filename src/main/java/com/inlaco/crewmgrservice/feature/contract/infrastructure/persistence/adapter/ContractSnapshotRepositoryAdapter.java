package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.adapter;

import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractSnapshotRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.mapper.ContractEntityMapper;
import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.repository.ContractSnapshotMongoRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ContractSnapshotRepositoryAdapter implements ContractSnapshotRepository {
  private final ContractSnapshotMongoRepository repository;
  private final ContractEntityMapper mapper;

  @Override
  public AbstractContract save(AbstractContract contract) {
    return mapper.toContract(repository.save(mapper.toSnapshot(contract)));
  }

  @Override
  public List<AbstractContract> findByContractId(String contractId) {
    return repository.findByContractId(new ObjectId(contractId)).stream()
        .map(mapper::toContract)
        .toList();
  }

  @Override
  public Optional<AbstractContract> findByContractIdAndVersion(String contractId, int version) {
    return repository
        .findByContractIdAndVersion(new ObjectId(contractId), version)
        .map(mapper::toContract);
  }
}
