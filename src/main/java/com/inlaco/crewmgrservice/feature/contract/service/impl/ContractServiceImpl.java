package com.inlaco.crewmgrservice.feature.contract.service.impl;

import com.inlaco.crewmgrservice.feature.contract.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.service.ContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractServiceImpl implements ContractService {
  @Override
  public Contract getContractById(String id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getContractById'");
  }

  @Override
  public Contract getContractBySailorId(String sailorId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getContractBySailorId'");
  }

  @Override
  public Contract updateContract(String contractId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateContract'");
  }
}
