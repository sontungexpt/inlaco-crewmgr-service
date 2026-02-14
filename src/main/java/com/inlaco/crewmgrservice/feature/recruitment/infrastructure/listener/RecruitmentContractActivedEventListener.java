package com.inlaco.crewmgrservice.feature.recruitment.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractActivedEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.recruitment.application.port.in.RecruitmentReviewUseCase;
import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecruitmentContractActivedEventListener {

  private final RecruitmentReviewUseCase recruitmentReviewUseCase;

  @TransactionalEventListener(
      value = ContractActivedEvent.class,
      phase = TransactionPhase.AFTER_COMMIT)
  public void handle(ContractActivedEvent event) {
    if (!(event.contract() instanceof LaborContract contract)) {
      return;
    }

    recruitmentReviewUseCase.reviewApplication(
        contract.getApplicationId(), ApplicationStatus.HIRED);
  }
}
