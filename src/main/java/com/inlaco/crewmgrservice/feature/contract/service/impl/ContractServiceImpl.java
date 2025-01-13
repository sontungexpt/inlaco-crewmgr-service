package com.inlaco.crewmgrservice.feature.contract.service.impl;

import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.contract.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.model.ContractVersion;
import com.inlaco.crewmgrservice.feature.contract.repository.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.repository.ContractVersionRepository;
import com.inlaco.crewmgrservice.feature.contract.service.ContractService;
import com.inlaco.crewmgrservice.feature.user.service.SailorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractServiceImpl implements ContractService {

  private final ContractRepository contractRepository;
  private final ContractVersionRepository contractVersionRepository;
  private final SailorService sailorService;

  @Override
  public Contract getContractById(String id) {
    return contractRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(AbstractContract.class, "id", id));
  }

  @Override
  public Contract getSailorContract(String sailorId) {
    var sailor = sailorService.findSailorProfileById(sailorId);
    if (!sailor.hasContract()) {
      throw new ResourceNotFoundException(AbstractContract.class, "sailorId", sailorId);
    }
    return getContractById(sailor.getContractId().toHexString());
  }

  @Override
  public Contract addContract(AbstractContract contract) {
    throw new UnsupportedOperationException("Unimplemented method 'addContract'");
  }

  @Override
  public Contract saveContract(AbstractContract contract) {
    return contractRepository.save(contract);
  }

  @Override
  public ContractVersion saveVersion(ContractVersion contract) {
    return contractVersionRepository.save(contract);
  }

  @Override
  public Contract createLaborContract(String sailorId, AbstractContract contract) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createLaborContract'");
  }
}
