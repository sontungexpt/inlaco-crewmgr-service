package com.inlaco.crewmgrservice.feature.crewrental.infrastructure.listener;

import com.inlaco.crewmgrservice.application.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractSignedEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.model.CrewSupplyContract;
import com.inlaco.crewmgrservice.feature.crewrental.application.port.out.CrewRentalRequestRepository;
import com.inlaco.crewmgrservice.feature.crewrental.domain.enums.CrewRentalRequestStatus;
import com.inlaco.crewmgrservice.feature.crewrental.domain.model.CrewRentalRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class CrewRentalContractSignedEventListener {

  private final CrewRentalRequestRepository crewRentalRequestRepository;

  @TransactionalEventListener(
      value = ContractSignedEvent.class,
      phase = TransactionPhase.AFTER_COMMIT)
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
