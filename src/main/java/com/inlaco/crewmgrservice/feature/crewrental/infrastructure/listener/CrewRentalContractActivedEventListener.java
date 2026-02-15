package com.inlaco.crewmgrservice.feature.crewrental.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractActivedEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.model.CrewSupplyContract;
import com.inlaco.crewmgrservice.feature.crewrental.application.port.out.CrewRentalRequestRepository;
import com.inlaco.crewmgrservice.feature.crewrental.domain.enums.CrewRentalRequestStatus;
import com.inlaco.crewmgrservice.feature.crewrental.domain.model.CrewRentalRequest;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class CrewRentalContractActivedEventListener {

  private final CrewRentalRequestRepository crewRentalRequestRepository;

  @TransactionalEventListener(
      value = ContractActivedEvent.class,
      phase = TransactionPhase.AFTER_COMMIT)
  public void handle(ContractActivedEvent event) {
    if (!(event.contract() instanceof CrewSupplyContract supplyContract)) {
      return;
    }

    String requestId = supplyContract.getCrewRentalRequestId();
    CrewRentalRequest request =
        crewRentalRequestRepository
            .findById(requestId)
            .orElseThrow(
                () -> new ResourceNotFoundException(CrewRentalRequest.class, "id", requestId));

    request.setStatus(CrewRentalRequestStatus.ACTIVE);
    crewRentalRequestRepository.save(request);
  }
}
