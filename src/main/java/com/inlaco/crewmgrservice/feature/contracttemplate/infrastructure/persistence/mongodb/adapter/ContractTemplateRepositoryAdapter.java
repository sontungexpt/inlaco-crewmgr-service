package com.inlaco.crewmgrservice.feature.contracttemplate.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.contracttemplate.application.port.out.ContractTemplateRepository;
import com.inlaco.crewmgrservice.feature.contracttemplate.domain.model.ContractTemplate;
import com.inlaco.crewmgrservice.feature.contracttemplate.infrastructure.persistence.mongodb.entity.ContractTemplateEntity;
import com.inlaco.crewmgrservice.feature.contracttemplate.infrastructure.persistence.mongodb.mapper.ContractTemplateEntityMapper;
import com.inlaco.crewmgrservice.feature.contracttemplate.infrastructure.persistence.mongodb.repository.ContractTemplateMongoRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ContractTemplateRepositoryAdapter implements ContractTemplateRepository {

  private final ContractTemplateMongoRepository repository;
  private final ContractTemplateEntityMapper mapper;

  @Override
  public Page<ContractTemplate> findByType(String type, Pageable pageable) {
    return repository.findByType(type, pageable).map(mapper::toContractTemplate);
  }

  @Override
  public Optional<ContractTemplate> findById(String id) {
    return repository.findById(id).map(mapper::toContractTemplate);
  }

  @Override
  public ContractTemplate save(ContractTemplate template) {
    ContractTemplateEntity entity;
    if (template.getId() == null) {
      // INSERT
      entity = mapper.toContractTemplateEntity(template);
    } else {
      entity =
          repository
              .findById(template.getId())
              .map(
                  existing -> {
                    mapper.updateFromContractTemplate(template, existing);
                    return existing;
                  })
              .orElseGet(() -> mapper.toContractTemplateEntity(template));
    }

    return mapper.toContractTemplate(repository.save(entity));
  }

  @Override
  public Page<ContractTemplate> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(mapper::toContractTemplate);
  }

  @Override
  public void deleteById(String id) {
    repository.deleteById(id);
  }
}
