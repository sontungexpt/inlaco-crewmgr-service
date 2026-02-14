package com.inlaco.crewmgrservice.feature.contract.application.port.out;

import com.inlaco.crewmgrservice.feature.contract.application.model.ContractSearchCriteria;
import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContractRepository {
  Optional<AbstractContract> findById(String id);

  AbstractContract save(AbstractContract contract);

  List<AbstractContract> saveAll(Iterable<AbstractContract> contracts);

  Page<AbstractContract> findAll(Pageable pageable);

  Page<AbstractContract> findAll(ContractSearchCriteria filterable, Pageable pageable);

  List<AbstractContract> findDueForActivation(Instant now);

  List<AbstractContract> findDueForExpiration(Instant now);
}
