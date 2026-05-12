package com.inlaco.crewmgrservice.feature.company.application.port.out;

import com.inlaco.crewmgrservice.feature.company.application.model.CompanySearchCriteria;
import com.inlaco.crewmgrservice.feature.company.domain.model.Company;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyRepository {
  Company save(Company company);
  Optional<Company> findById(String id);
  Optional<Company> findByRegistrationNumber(String registrationNumber);
  Page<Company> findAll(CompanySearchCriteria criteria, Pageable pageable);
  List<Company> findByStatus(Company.CompanyStatus status);
  void deleteById(String id);
  boolean existsByRegistrationNumber(String registrationNumber);
}
