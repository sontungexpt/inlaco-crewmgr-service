package com.inlaco.crewmgrservice.feature.contract.application.service;

import com.inlaco.crewmgrservice.feature.contract.application.port.in.LaborContractQueryUseCase;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.LaborContractRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaborContractQueryService implements LaborContractQueryUseCase {

  private final LaborContractRepository contractRepository;

  @Override
  public Contract getContractByApplicationId(String applicationId) {
    return contractRepository
        .findByApplicationId(applicationId)
        .orElseThrow(
            () -> new ResourceNotFoundException(Contract.class, "applicationId", applicationId));
  }
}
