package com.inlaco.crewmgrservice.feature.contract.application.port.out;

import com.inlaco.crewmgrservice.feature.contract.application.model.ContractSearchCriteria;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContractRepository {
  Optional<Contract> findById(String id);

  Contract save(Contract contract);

  List<Contract> saveAll(Iterable<Contract> contracts);

  Page<Contract> findAll(Pageable pageable);

  Page<Contract> findAll(ContractSearchCriteria filterable, Pageable pageable);

  List<Contract> findDueForActivation(Instant now);

  List<Contract> findDueForExpiration(Instant now);
}
