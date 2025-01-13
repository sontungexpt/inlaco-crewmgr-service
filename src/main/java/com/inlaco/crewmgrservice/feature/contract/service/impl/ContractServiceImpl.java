package com.inlaco.crewmgrservice.feature.contract.service.impl;

import com.inlaco.crewmgrservice.feature.contract.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.repository.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.repository.ContractVersionRepository;
import com.inlaco.crewmgrservice.feature.contract.service.ContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractServiceImpl implements ContractService {

  private final ContractRepository contractRepository;
  private final ContractVersionRepository contractVersionRepository;

  @Override
  public Contract getContractById(String id) {
    throw new UnsupportedOperationException("Unimplemented method 'getContractById'");
  }

  @Override
  public Contract getContractBySailorId(String sailorId) {
    throw new UnsupportedOperationException("Unimplemented method 'getContractBySailorId'");
  }

  @Override
  public Contract updateContract(String contractId) {
    throw new UnsupportedOperationException("Unimplemented method 'updateContract'");
  }

  @Override
  public Contract addContract(Contract contract) {
    throw new UnsupportedOperationException("Unimplemented method 'addContract'");
  }

  @Override
  public Contract saveContract(Contract contract) {
    throw new UnsupportedOperationException("Unimplemented method 'saveContract'");
  }
}
