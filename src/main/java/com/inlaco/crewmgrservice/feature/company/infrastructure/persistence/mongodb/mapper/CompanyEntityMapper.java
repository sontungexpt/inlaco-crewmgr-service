package com.inlaco.crewmgrservice.feature.company.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.company.application.model.CompanySearchCriteria;
import com.inlaco.crewmgrservice.feature.company.domain.model.Company;
import com.inlaco.crewmgrservice.feature.company.infrastructure.persistence.mongodb.entity.CompanyEntity;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyEntityMapper {

  private final MongoTemplate mongoTemplate;

  public CompanyEntityMapper(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public Company toDomain(CompanyEntity entity) {
    if (entity == null) {
      return null;
    }

    return Company.builder()
        .id(entity.getIdAsString())
        .name(entity.getName())
        .description(entity.getDescription())
        .registrationNumber(entity.getRegistrationNumber())
        .taxId(entity.getTaxId())
        .address(entity.getAddress())
        .phoneNumber(entity.getPhoneNumber())
        .email(entity.getEmail())
        .website(entity.getWebsite())
        .logo(entity.getLogo())
        .status(entity.getStatus())
        .createdBy(entity.getCreatedBy())
        .createdAt(entity.getCreatedAt() != null ? java.time.Instant.ofEpochSecond(entity.getCreatedAt()) : null)
        .updatedBy(entity.getUpdatedBy())
        .updatedAt(entity.getUpdatedAt() != null ? java.time.Instant.ofEpochSecond(entity.getUpdatedAt()) : null)
        .build();
  }

  public CompanyEntity toEntity(Company domain) {
    if (domain == null) {
      return null;
    }

    return CompanyEntity.builder()
        .id(domain.getId() != null ? new ObjectId(domain.getId()) : null)
        .name(domain.getName())
        .description(domain.getDescription())
        .registrationNumber(domain.getRegistrationNumber())
        .taxId(domain.getTaxId())
        .address(domain.getAddress())
        .phoneNumber(domain.getPhoneNumber())
        .email(domain.getEmail())
        .website(domain.getWebsite())
        .logo(domain.getLogo())
        .status(domain.getStatus())
        .createdBy(domain.getCreatedBy())
        .createdAt(domain.getCreatedAt() != null ? domain.getCreatedAt().getEpochSecond() : null)
        .updatedBy(domain.getUpdatedBy())
        .updatedAt(domain.getUpdatedAt() != null ? domain.getUpdatedAt().getEpochSecond() : null)
        .build();
  }

  public List<Company> toDomainList(List<CompanyEntity> entities) {
    return entities.stream()
        .map(this::toDomain)
        .collect(Collectors.toList());
  }

  public Page<Company> toDomainPage(Page<CompanyEntity> entityPage) {
    List<Company> companies = toDomainList(entityPage.getContent());
    return new PageImpl<>(companies, entityPage.getPageable(), entityPage.getTotalElements());
  }

  public Query buildSearchQuery(CompanySearchCriteria criteria, Pageable pageable) {
    Query query = new Query();

    if (criteria != null) {
      if (criteria.getName() != null && !criteria.getName().trim().isEmpty()) {
        query.addCriteria(Criteria.where("name").regex(criteria.getName(), "i"));
      }
      if (criteria.getRegistrationNumber() != null && !criteria.getRegistrationNumber().trim().isEmpty()) {
        query.addCriteria(Criteria.where("registration_number").is(criteria.getRegistrationNumber()));
      }
      if (criteria.getStatus() != null) {
        query.addCriteria(Criteria.where("status").is(criteria.getStatus()));
      }
      if (criteria.getEmail() != null && !criteria.getEmail().trim().isEmpty()) {
        query.addCriteria(Criteria.where("email").regex(criteria.getEmail(), "i"));
      }
      if (criteria.getPhoneNumber() != null && !criteria.getPhoneNumber().trim().isEmpty()) {
        query.addCriteria(Criteria.where("phone_number").is(criteria.getPhoneNumber()));
      }
    }

    // Apply pagination
    if (pageable != null) {
      query.skip(pageable.getOffset());
      query.limit(pageable.getPageSize());
    }

    return query;
  }
}
