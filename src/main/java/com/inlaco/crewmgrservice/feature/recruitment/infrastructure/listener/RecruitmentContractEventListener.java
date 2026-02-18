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

  @TransactionalEventListener
  public void handleActived(ContractActivedEvent event) {
    for (var c : event.contracts()) {
      if (!(c instanceof LaborContract contract)) continue;
      recruitmentReviewUseCase.reviewApplication(
          contract.getApplicationId(), ApplicationStatus.HIRED);
    }
  }

  @TransactionalEventListener
  public void handleCreated(ContractCreatedEvent event) {
    if (!(event.contract() instanceof LaborContract contract)) return;
    recruitmentReviewUseCase.reviewApplication(
        contract.getApplicationId(), ApplicationStatus.CONTRACT_PENDING_SIGNATURE);
  }

  @TransactionalEventListener
  public void handleSigned(ContractSignedEvent event) {
    if (!(event.contract() instanceof LaborContract contract)) return;
    recruitmentReviewUseCase.reviewApplication(
        contract.getApplicationId(), ApplicationStatus.CONTRACT_SIGNED);
  }
}
