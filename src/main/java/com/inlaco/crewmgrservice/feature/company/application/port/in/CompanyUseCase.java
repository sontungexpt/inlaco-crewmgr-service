package com.inlaco.crewmgrservice.feature.company.application.port.in;

import com.inlaco.crewmgrservice.feature.company.domain.model.Company;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyUseCase {
  Company createCompany(Company company, User authenticatedUser);
  Company updateCompany(String companyId, Company company, User authenticatedUser);
  void deleteCompany(String companyId, User authenticatedUser);
  Company getCompany(String companyId);
  Page<Company> getCompanies(String search, Pageable pageable);
  List<Company> getActiveCompanies();
  Company changeCompanyStatus(String companyId, Company.CompanyStatus status, User authenticatedUser);
}
