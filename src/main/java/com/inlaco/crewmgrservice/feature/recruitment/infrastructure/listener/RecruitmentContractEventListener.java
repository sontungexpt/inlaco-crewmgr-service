package com.inlaco.crewmgrservice.feature.recruitment.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractActivedEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractCreatedEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractSignedEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.recruitment.application.port.in.RecruitmentReviewUseCase;
import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecruitmentContractEventListener {

  private final RecruitmentReviewUseCase recruitmentReviewUseCase;

  @TransactionalEventListener(ContractActivedEvent.class)
  public void handleActived(ContractActivedEvent event) {
    int total = event.contracts().size();
    log.info("Handling ContractActivedEvent with {} contract(s)", total);

    for (var c : event.contracts()) {
      if (!(c instanceof LaborContract contract)) {
        // Non-labor contracts are not relevant for recruitment review, keep this at debug.
        log.debug("Skipping non-LaborContract (id={}) during actived handling", c.getId());
        continue;
      }

      // Provide a debug-level log with the application id being reviewed to avoid noisy logs
      // at info level when multiple contracts are processed frequently.
      log.debug(
          "Reviewing application {} due to activated labor contract {}",
          contract.getApplicationId(),
          contract.getId());

      recruitmentReviewUseCase.reviewApplication(
          contract.getApplicationId(), ApplicationStatus.HIRED);
    }

    log.info("Completed processing ContractActivedEvent; processed {} labor contract(s)", total);
  }

  @TransactionalEventListener(ContractCreatedEvent.class)
  public void handleCreated(ContractCreatedEvent event) {
    var contract = event.contract();
    if (contract == null) {
      log.warn("Received ContractCreatedEvent with null contract payload");
      return;
    }

    log.info(
        "Handling ContractCreatedEvent for contract id={} type={}",
        contract.getId(),
        contract.getClass().getSimpleName());

    if (!(contract instanceof LaborContract laborContract)) {
      log.debug(
          "Contract {} is not a LaborContract; skipping recruitment review", contract.getId());
      return;
    }

    log.debug(
        "Requesting review for application {} -> {}",
        laborContract.getApplicationId(),
        ApplicationStatus.CONTRACT_PENDING_SIGNATURE);

    recruitmentReviewUseCase.reviewApplication(
        laborContract.getApplicationId(), ApplicationStatus.CONTRACT_PENDING_SIGNATURE);

    log.info(
        "Requested recruitment review for application {} (contract id={})",
        laborContract.getApplicationId(),
        laborContract.getId());
  }

  @TransactionalEventListener(ContractSignedEvent.class)
  public void handleSigned(ContractSignedEvent event) {
    var contract = event.contract();
    if (contract == null) {
      log.warn("Received ContractSignedEvent with null contract payload");
      return;
    }

    log.info(
        "Handling ContractSignedEvent for contract id={} type={}",
        contract.getId(),
        contract.getClass().getSimpleName());

    if (!(contract instanceof LaborContract laborContract)) {
      log.debug(
          "Contract {} is not a LaborContract; skipping recruitment review", contract.getId());
      return;
    }

    log.debug(
        "Requesting review for application {} -> {}",
        laborContract.getApplicationId(),
        ApplicationStatus.CONTRACT_SIGNED);

    recruitmentReviewUseCase.reviewApplication(
        laborContract.getApplicationId(), ApplicationStatus.CONTRACT_SIGNED);

    log.info(
        "Requested recruitment review for application {} (signed contract id={})",
        laborContract.getApplicationId(),
        laborContract.getId());
  }
}
