package com.inlaco.crewmgrservice.feature.contract.service;

import com.inlaco.crewmgrservice.feature.contract.model.ContractTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContractTemplateService {
  ContractTemplate getTemplateById(String id);

  Page<ContractTemplate> getAllTemplates(String type, Pageable pageable);

  ContractTemplate saveTemplate(ContractTemplate template);

  ContractTemplate uploadTemplate(String templateFilePubId, ContractTemplate template);

  void removeTemplate(String id);
}
