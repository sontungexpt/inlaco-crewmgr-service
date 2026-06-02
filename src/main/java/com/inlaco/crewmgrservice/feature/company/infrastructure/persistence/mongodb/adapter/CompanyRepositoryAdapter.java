package com.inlaco.crewmgrservice.feature.company.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.company.application.model.CompanySearchCriteria;
import com.inlaco.crewmgrservice.feature.company.application.port.out.CompanyRepository;
import com.inlaco.crewmgrservice.feature.company.domain.model.Company;
import com.inlaco.crewmgrservice.feature.company.infrastructure.persistence.mongodb.entity.CompanyEntity;
import com.inlaco.crewmgrservice.feature.company.infrastructure.persistence.mongodb.mapper.CompanyEntityMapper;
import com.inlaco.crewmgrservice.feature.company.infrastructure.persistence.mongodb.repository.CompanyMongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CompanyRepositoryAdapter implements CompanyRepository {

  private final CompanyMongoRepository mongoRepository;
  private final CompanyEntityMapper entityMapper;
  private final MongoTemplate mongoTemplate;

  @Override
  public Company save(Company company) {
    log.debug("Saving company with registration number: {}", company.getRegistrationNumber());
    CompanyEntity entity = entityMapper.toEntity(company);
    CompanyEntity savedEntity = mongoRepository.save(entity);
    return entityMapper.toDomain(savedEntity);
  }

  @Override
  public Optional<Company> findById(String id) {
    log.debug("Finding company by ID: {}", id);
    return mongoRepository.findById(id)
        .map(entityMapper::toDomain);
  }

  @Override
  public Optional<Company> findByRegistrationNumber(String registrationNumber) {
    log.debug("Finding company by registration number: {}", registrationNumber);
    return mongoRepository.findByRegistrationNumber(registrationNumber)
        .map(entityMapper::toDomain);
  }

  @Override
  public Page<Company> findAll(CompanySearchCriteria criteria, Pageable pageable) {
    log.debug("Finding companies with criteria: {}", criteria);
    var query = entityMapper.buildSearchQuery(criteria, pageable);
    var entities = mongoTemplate.find(query, CompanyEntity.class);
    var countQuery = entityMapper.buildSearchQuery(criteria, null);
    var total = mongoTemplate.count(countQuery, CompanyEntity.class);
    
    var companyEntities = entities.stream()
        .map(entityMapper::toDomain)
        .toList();
    
    return new org.springframework.data.domain.PageImpl<>(companyEntities, pageable, total);
  }

  @Override
  public List<Company> findByStatus(Company.CompanyStatus status) {
    log.debug("Finding companies by status: {}", status);
    var entities = mongoRepository.findByStatus(status);
    return entityMapper.toDomainList(entities);
  }

  @Override
  public void deleteById(String id) {
    log.debug("Deleting company by ID: {}", id);
    mongoRepository.deleteById(id);
  }

  @Override
  public boolean existsByRegistrationNumber(String registrationNumber) {
    log.debug("Checking if company exists by registration number: {}", registrationNumber);
    return mongoRepository.existsByRegistrationNumber(registrationNumber);
  }
}
