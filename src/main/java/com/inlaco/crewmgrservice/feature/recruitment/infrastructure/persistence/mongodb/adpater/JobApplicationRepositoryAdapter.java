package com.inlaco.crewmgrservice.feature.recruitment.infrastructure.persistence.mongodb.adpater;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.common.model.FacetResult;
import com.inlaco.crewmgrservice.feature.recruitment.application.model.JobApplicationSearchCriteria;
import com.inlaco.crewmgrservice.feature.recruitment.application.port.out.JobApplicationRepository;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.feature.recruitment.infrastructure.persistence.mongodb.entity.JobApplicationEntity;
import com.inlaco.crewmgrservice.feature.recruitment.infrastructure.persistence.mongodb.mapper.JobApplicationEntityMapper;
import com.inlaco.crewmgrservice.feature.recruitment.infrastructure.persistence.mongodb.repository.JobApplicationMongoRepository;
import com.inlaco.crewmgrservice.utils.PageableUtils;
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
  private final JobApplicationEntityMapper jobApplicationEntityMapper;
  private final JobApplicationMongoRepository jobApplicationMongoRepository;

  @Override
  public JobApplication save(JobApplication jobApplication) {
    return jobApplicationEntityMapper.toJobApplication(
        jobApplicationMongoRepository.save(
            jobApplicationEntityMapper.toJobApplicationEntity(jobApplication)));
  }

  @Override
  public Page<JobApplication> findByAccountId(String accountId, Pageable pageable) {
    return jobApplicationMongoRepository
        .findByAccountId(new ObjectId(accountId), pageable)
        .map(jobApplicationEntityMapper::toJobApplication);
  }

  @Override
  public Optional<JobApplication> findById(String id) {
    return jobApplicationMongoRepository
        .findById(id)
        .map(jobApplicationEntityMapper::toJobApplication);
  }

  @Override
  public Page<JobApplication> findAll(Pageable pageable) {
    return jobApplicationMongoRepository
        .findAll(pageable)
        .map(jobApplicationEntityMapper::toJobApplication);
  }

  @Override
  public Page<JobApplication> findAll(JobApplicationSearchCriteria criteria, Pageable pageable) {
    pageable = PageableUtils.extendDefaultSort(pageable);
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
        .map(jobApplicationEntityMapper::toJobApplication);
  }

  static class JobApplicationEntityFacetResult extends FacetResult<JobApplicationEntity> {}
}
