package com.inlaco.crewmgrservice.feature.contract.application.service;

import com.inlaco.crewmgrservice.feature.contract.application.model.ContractAssets;
import com.inlaco.crewmgrservice.feature.contract.application.port.in.CreateLaborContractUseCase;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.LaborContractRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.recruitment.application.port.in.RecruitmentQueryUseCase;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadDispatcher;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceAlreadyInUseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateLaborContractService implements CreateLaborContractUseCase {

  private final RecruitmentQueryUseCase recruitmentQueryUseCase;
  private final ContractRepository contractRepository;
  private final LaborContractRepository laborContractRepository;
  private final UploadDispatcher uploadDispatcher;

  @Override
  public AbstractContract create(
      String applicationId, LaborContract contract, ContractAssets assets, User creator) {
    if (laborContractRepository.existsByApplicationId(applicationId)) {
      throw new ResourceAlreadyInUseException(LaborContract.class, "applicationId", applicationId);
    }

    JobApplication jobApplication = recruitmentQueryUseCase.getApplicationDetail(applicationId);

    String accountId = jobApplication.getAccountId();

    contract.setContractFile(
        uploadDispatcher.fetch(AssetType.CONTRACT_FILE, assets.getContractFile()));

    contract.setApplicationId(applicationId);
    contract.setAccountId(accountId);

    var newContract = contractRepository.save(contract);

    log.info("Created labor contract for sailor with account id: {}", accountId);

    return newContract;
  }
}
