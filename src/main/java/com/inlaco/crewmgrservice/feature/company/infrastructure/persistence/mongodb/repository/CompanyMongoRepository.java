package com.inlaco.crewmgrservice.feature.company.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.company.domain.model.Company;
import com.inlaco.crewmgrservice.feature.company.infrastructure.persistence.mongodb.entity.CompanyEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyMongoRepository extends MongoRepository<CompanyEntity, String> {

  Optional<CompanyEntity> findByRegistrationNumber(String registrationNumber);

  boolean existsByRegistrationNumber(String registrationNumber);

  @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
  java.util.List<CompanyEntity> findByNameContainingIgnoreCase(String name);

  java.util.List<CompanyEntity> findByStatus(Company.CompanyStatus status);
}
