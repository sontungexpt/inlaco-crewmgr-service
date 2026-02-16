package com.inlaco.crewmgrservice.feature.crewrental.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractSignedEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.model.CrewSupplyContract;
import com.inlaco.crewmgrservice.feature.crewrental.application.port.out.CrewRentalRequestRepository;
import com.inlaco.crewmgrservice.feature.crewrental.domain.enums.CrewRentalRequestStatus;
import com.inlaco.crewmgrservice.feature.crewrental.domain.model.CrewRentalRequest;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class CrewRentalContractSignedEventListener {

  private final CrewRentalRequestRepository crewRentalRequestRepository;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Retryable(
      retryFor = Exception.class,
      maxAttempts = 5,
      backoff = @Backoff(delay = 2000, multiplier = 2))
  public void handle(ContractSignedEvent event) {
    if (!(event.contract() instanceof CrewSupplyContract supplyContract)) {
      return;
    }

    String requestId = supplyContract.getCrewRentalRequestId();
    CrewRentalRequest request =
        crewRentalRequestRepository
            .findById(requestId)
            .orElseThrow(
                () -> new ResourceNotFoundException(CrewRentalRequest.class, "id", requestId));

    request.setStatus(CrewRentalRequestStatus.CONFIRMED);
    crewRentalRequestRepository.save(request);
  }
}
