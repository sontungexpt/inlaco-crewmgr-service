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
    log.debug("Received contract actived event");
    for (var c : event.contracts()) {
      if (!(c instanceof LaborContract contract)) continue;
      recruitmentReviewUseCase.reviewApplication(
          contract.getApplicationId(), ApplicationStatus.HIRED);
    }
  }

  @TransactionalEventListener(ContractCreatedEvent.class)
  public void handleCreated(ContractCreatedEvent event) {
    log.debug("Received contract created event");
    if (!(event.contract() instanceof LaborContract contract)) return;
    recruitmentReviewUseCase.reviewApplication(
        contract.getApplicationId(), ApplicationStatus.CONTRACT_PENDING_SIGNATURE);
  }

  @TransactionalEventListener(ContractSignedEvent.class)
  public void handleSigned(ContractSignedEvent event) {
    log.debug("Received contract signed event");
    if (!(event.contract() instanceof LaborContract contract)) return;
    recruitmentReviewUseCase.reviewApplication(
        contract.getApplicationId(), ApplicationStatus.CONTRACT_SIGNED);
  }
}
