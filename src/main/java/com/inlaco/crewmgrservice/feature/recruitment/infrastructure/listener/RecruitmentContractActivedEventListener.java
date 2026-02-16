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

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(ContractActivedEvent event) {
    for (var c : event.contracts()) {
      if (!(c instanceof LaborContract contract)) continue;
      recruitmentReviewUseCase.reviewApplication(
          contract.getApplicationId(), ApplicationStatus.HIRED);
    }
  }
}
