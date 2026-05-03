package com.inlaco.crewmgrservice.feature.contract.application.service;

import com.inlaco.crewmgrservice.feature.contract.application.model.ContractAssets;
import com.inlaco.crewmgrservice.feature.contract.application.port.in.CreateLaborContractUseCase;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.LaborContractRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractCreatedEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.party.Party;
import com.inlaco.crewmgrservice.feature.recruitment.application.port.in.JobApplicationQueryUseCase;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadDispatcher;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceAlreadyInUseException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateLaborContractService implements CreateLaborContractUseCase {

  private final JobApplicationQueryUseCase recruitmentQueryUseCase;
  private final ContractRepository contractRepository;
  private final LaborContractRepository laborContractRepository;
  private final UploadDispatcher uploadDispatcher;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public Contract create(
      String applicationId, LaborContract contract, ContractAssets assets, User creator) {
    if (laborContractRepository.existsByApplicationId(applicationId)) {
      throw new ResourceAlreadyInUseException(LaborContract.class, "applicationId", applicationId);
    }

    JobApplication jobApplication = recruitmentQueryUseCase.getApplicationDetail(applicationId);

    String accountId = jobApplication.getAccountId();

    log.debug("Fetching contract file for labor contract creation.");
    contract.setContractFile(
        uploadDispatcher.fetch(AssetType.CONTRACT_FILE, assets.getContractFile()));

    List<String> attachments = assets.getAttachments();
    if (attachments != null && !attachments.isEmpty()) {
      log.debug("Fetching attachments for labor contract creation.");
      contract.setAttachments(
          attachments.stream()
              .map(attachment -> uploadDispatcher.fetch(AssetType.CONTRACT_FILE, attachment))
              .toList());
    }

    contract.setApplicationId(applicationId);
    contract.setAccountId(accountId);

    Party applicant = contract.getPartners().get(0);
    applicant.setAccountId(accountId);

    var newContract = contractRepository.save(contract);
    log.info("Publishing ContractCreatedEvent for labor contract with ID: {}", newContract.getId());
    eventPublisher.publishEvent(new ContractCreatedEvent(newContract));

    log.debug("Created labor contract for sailor with account id: {}", accountId);

    return newContract;
  }
}
