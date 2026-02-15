package com.inlaco.crewmgrservice.feature.contracttemplate.application.service;

import com.inlaco.crewmgrservice.application.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.contracttemplate.application.port.in.ContractTemplateUseCase;
import com.inlaco.crewmgrservice.feature.contracttemplate.application.port.out.ContractTemplateRepository;
import com.inlaco.crewmgrservice.feature.contracttemplate.domain.model.ContractTemplate;
import com.inlaco.crewmgrservice.feature.upload.application.enums.UploadStrategy;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadFactory;
import com.inlaco.crewmgrservice.shared.support.ConsoleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractTemplateService implements ContractTemplateUseCase {

  private final ContractTemplateRepository contractTemplateRepository;
  private final UploadFactory uploadFactory;

  @Override
  public ContractTemplate getTemplateById(String id) {
    return contractTemplateRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(ContractTemplate.class, "id", id));
  }

  @Override
  public Page<ContractTemplate> getAllTemplates(@Nullable String type, Pageable pageable) {
    if (type != null) return contractTemplateRepository.findByType(type, pageable);
    return contractTemplateRepository.findAll(pageable);
  }

  @Override
  public ContractTemplate uploadTemplate(String templateFilePubId, ContractTemplate template) {
    File metadata = uploadFactory.metadata(UploadStrategy.CONTRACT_TEMPLATE, templateFilePubId);
    ConsoleUtils.prettyPrint(metadata);
    template.setMetadata(metadata);
    return contractTemplateRepository.save(template);
  }

  @Override
  public void removeTemplate(String id) {
    contractTemplateRepository.deleteById(id);
  }
}
