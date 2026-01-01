package com.inlaco.crewmgrservice.feature.contract.service.impl;

import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.contract.model.ContractTemplate;
import com.inlaco.crewmgrservice.feature.contract.repository.ContractTemplateRepository;
import com.inlaco.crewmgrservice.feature.contract.service.ContractTemplateService;
import com.inlaco.crewmgrservice.feature.upload.enums.UploadStrategy;
import com.inlaco.crewmgrservice.feature.upload.service.UploadFactory;
import com.inlaco.crewmgrservice.utils.ConsoleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractTemplateServiceImpl implements ContractTemplateService {

  private final ContractTemplateRepository contractTemplateRepository;
  private final UploadFactory uploadFactory;

  @Override
  public ContractTemplate getTemplateById(String id) {
    return contractTemplateRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(ContractTemplate.class, "id", id));
  }

  @Override
  public Page<ContractTemplate> getAllTemplates(Pageable pageable) {
    return contractTemplateRepository.findAll(pageable);
  }

  @Override
  public ContractTemplate saveTemplate(ContractTemplate template) {
    return contractTemplateRepository.save(template);
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
