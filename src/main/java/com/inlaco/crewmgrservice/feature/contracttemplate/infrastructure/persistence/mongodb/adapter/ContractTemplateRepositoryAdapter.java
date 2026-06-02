package com.inlaco.crewmgrservice.feature.contracttemplate.infrastructure.persistence.mongodb.adapter;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.feature.contracttemplate.application.model.ContractTemplateSearchCriteria;
import com.inlaco.crewmgrservice.feature.contracttemplate.application.port.out.ContractTemplateRepository;
import com.inlaco.crewmgrservice.feature.contracttemplate.domain.model.ContractTemplate;
import com.inlaco.crewmgrservice.feature.contracttemplate.infrastructure.persistence.mongodb.entity.ContractTemplateEntity;
import com.inlaco.crewmgrservice.feature.contracttemplate.infrastructure.persistence.mongodb.mapper.ContractTemplateEntityMapper;
import com.inlaco.crewmgrservice.feature.contracttemplate.infrastructure.persistence.mongodb.repository.ContractTemplateMongoRepository;
import com.inlaco.crewmgrservice.infrastructure.persistence.mongodb.aggregation.FacetResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ContractTemplateRepositoryAdapter implements ContractTemplateRepository {

  private final ContractTemplateMongoRepository repository;
  private final ContractTemplateEntityMapper mapper;
  private final MongoTemplate mongoTemplate;

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

  @Override
  public Page<ContractTemplate> findAll(
      ContractTemplateSearchCriteria criteria, Pageable pageable) {

    List<Criteria> criteriaList = new ArrayList<>();

    if (criteria != null) {

      if (StringUtils.hasText(criteria.getType())) {
        criteriaList.add(Criteria.where("type").is(criteria.getType()));
      }

      if (StringUtils.hasText(criteria.getKeyword())) {
        String keyword = criteria.getKeyword();

        criteriaList.add(
            new Criteria()
                .orOperator(
                    Criteria.where("name").regex(keyword, "i"),
                    Criteria.where("description").regex(keyword, "i")));
      }
    }

    Criteria finalCriteria =
        criteriaList.isEmpty()
            ? new Criteria()
            : new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));

    Aggregation aggregation =
        newAggregation(
            match(finalCriteria),
            facet(Aggregation.count().as(FacetResult.COUNT_KEY))
                .as(FacetResult.COUNT_FACET_NAME)
                .and(
                    sort(pageable.getSort()),
                    skip(pageable.getOffset()),
                    limit(pageable.getPageSize()))
                .as(FacetResult.DATA_FACET_NAME));

    return mongoTemplate
        .aggregate(aggregation, ContractTemplateEntity.class, ContractTemplateFacetResult.class)
        .getUniqueMappedResult()
        .toPage(pageable)
        .map(mapper::toContractTemplate);
  }

  /* =========================================================================
   * FACET RESULT
   * ========================================================================= */

  private class ContractTemplateFacetResult extends FacetResult<ContractTemplateEntity> {}
}
