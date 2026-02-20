package com.inlaco.crewmgrservice.feature.recruitment.infrastructure.persistence.mongodb.adpater;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.feature.recruitment.application.model.JobApplicationSearchCriteria;
import com.inlaco.crewmgrservice.feature.recruitment.application.port.out.JobApplicationRepository;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.feature.recruitment.infrastructure.persistence.mongodb.entity.JobApplicationEntity;
import com.inlaco.crewmgrservice.feature.recruitment.infrastructure.persistence.mongodb.mapper.JobApplicationEntityMapper;
import com.inlaco.crewmgrservice.feature.recruitment.infrastructure.persistence.mongodb.repository.JobApplicationMongoRepository;
import com.inlaco.crewmgrservice.infrastructure.persistence.mongodb.aggregation.FacetResult;
import com.inlaco.crewmgrservice.infrastructure.persistence.support.PageableUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JobApplicationRepositoryAdapter implements JobApplicationRepository {

  private final MongoTemplate mongoTemplate;
  private final JobApplicationEntityMapper mapper;
  private final JobApplicationMongoRepository repository;

  @Override
  public JobApplication save(JobApplication jobApplication) {
    String id = jobApplication.getId();
    JobApplicationEntity entity;
    if (id == null) {
      // INSERT
      entity = mapper.toJobApplicationEntity(jobApplication);
    } else {
      entity =
          repository
              .findById(id)
              .map(
                  existing -> {
                    mapper.updateFromJobApplication(jobApplication, existing);
                    return existing;
                  })
              .orElseGet(() -> mapper.toJobApplicationEntity(jobApplication));
    }

    return mapper.toJobApplication(repository.save(entity));
  }

  @Override
  public Page<JobApplication> findByAccountId(String accountId, Pageable pageable) {
    return repository
        .findByAccountId(new ObjectId(accountId), pageable)
        .map(mapper::toJobApplication);
  }

  @Override
  public Optional<JobApplication> findById(String id) {
    return repository.findById(id).map(mapper::toJobApplication);
  }

  @Override
  public Page<JobApplication> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(mapper::toJobApplication);
  }

  @Override
  public Page<JobApplication> findAll(JobApplicationSearchCriteria criteria, Pageable pageable) {
    var query = new Criteria();

    if (criteria != null) {
      if (StringUtils.hasText(criteria.getRecruitmentPostId())) {
        query.and("recruitmentPostId").is(new ObjectId(criteria.getRecruitmentPostId()));
      }
      if (StringUtils.hasText(criteria.getAccountId())) {
        query.and("accountId").is(new ObjectId(criteria.getAccountId()));
      }
      if (criteria.getStatus() != null) {
        query.and("status").is(criteria.getStatus());
      }
    }

    pageable = PageableUtils.enforceIdSort(pageable);
    Aggregation aggregation =
        newAggregation(
            match(query),
            facet(Aggregation.count().as(FacetResult.COUNT_KEY))
                .as(FacetResult.COUNT_FACET_NAME)
                .and(
                    sort(pageable.getSort()),
                    skip(pageable.getOffset()),
                    limit(pageable.getPageSize()))
                .as(FacetResult.DATA_FACET_NAME));

    return mongoTemplate
        .aggregate(aggregation, JobApplicationEntity.class, JobApplicationEntityFacetResult.class)
        .getUniqueMappedResult()
        .toPage(pageable)
        .map(mapper::toJobApplication);
  }

  static class JobApplicationEntityFacetResult extends FacetResult<JobApplicationEntity> {}
}
