package com.inlaco.crewmgrservice.feature.recruitment.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractSignedEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.recruitment.application.port.in.RecruitmentReviewUseCase;
import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecruitmentContractSignedEventListener {

  private final RecruitmentReviewUseCase recruitmentReviewUseCase;

  @TransactionalEventListener(
      value = ContractSignedEvent.class,
      phase = TransactionPhase.AFTER_COMMIT)
  public void handle(ContractSignedEvent event) {
    if (!(event.contract() instanceof LaborContract contract)) {
      return;
    }

    recruitmentReviewUseCase.reviewApplication(
        contract.getApplicationId(), ApplicationStatus.CONFIRMED);
  }
}
