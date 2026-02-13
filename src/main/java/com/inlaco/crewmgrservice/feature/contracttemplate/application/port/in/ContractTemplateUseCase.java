package com.inlaco.crewmgrservice.feature.contracttemplate.application.port.in;

import com.inlaco.crewmgrservice.feature.contracttemplate.domain.model.ContractTemplate;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContractTemplateUseCase {

  ContractTemplate getTemplateById(String id);

  Page<ContractTemplate> getAllTemplates(@Nullable String type, Pageable pageable);

  ContractTemplate uploadTemplate(String templateFilePubId, ContractTemplate template);

  void removeTemplate(String id);
}
