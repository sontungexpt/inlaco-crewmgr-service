package com.inlaco.crewmgrservice.feature.contracttemplate.application.port.out;

import com.inlaco.crewmgrservice.feature.contracttemplate.domain.model.ContractTemplate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContractTemplateRepository {

  Optional<ContractTemplate> findById(String id);

  ContractTemplate save(ContractTemplate template);

  void deleteById(String id);

  Page<ContractTemplate> findAll(Pageable pageable);

  Page<ContractTemplate> findByType(String type, Pageable pageable);
}
