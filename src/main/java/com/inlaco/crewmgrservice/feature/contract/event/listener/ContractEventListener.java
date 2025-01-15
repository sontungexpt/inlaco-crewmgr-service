package com.inlaco.crewmgrservice.feature.contract.event.listener;

import com.inlaco.crewmgrservice.feature.contract.event.ContractActivedEvent;
import com.inlaco.crewmgrservice.feature.contract.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.model.SupplyContract;
import com.inlaco.crewmgrservice.feature.crewhiring.enums.CrewRentalRequestStatus;
import com.inlaco.crewmgrservice.feature.crewhiring.model.CrewRentalRequest;
import com.inlaco.crewmgrservice.feature.crewhiring.service.CrewRentalRequestService;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import com.inlaco.crewmgrservice.feature.user.service.SailorService;
import com.inlaco.crewmgrservice.feature.user.service.UserService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ContractEventListener {

  private final CrewRentalRequestService crewRentalRequestService;
  private final UserService userService;
  private final SailorService sailorService;

  @TransactionalEventListener(ContractActivedEvent.class)
  public void onContractActivedEvent(ContractActivedEvent event) {

    var contract = event.getContract();
    if (contract instanceof SupplyContract that) {
      CrewRentalRequest request =
          crewRentalRequestService.getRequestById(that.getRentalRequestId().toHexString());
      request.setStatus(CrewRentalRequestStatus.ACTIVE);
      crewRentalRequestService.saveRequest(request);
    } else if (contract instanceof LaborContract that) {
      SailorProfile sailorProfile =
          sailorService.findSailorProfileByAccountId(that.getEmployeeId().toHexString());
      sailorProfile.setCardId(sailorService.generateSailorCardId());
      sailorProfile.setJoinedAt(Instant.now());
      sailorService.saveSailorProfile(sailorProfile);
      userService.updateToSailor(that.getEmployeeId().toHexString());
    }
  }
}
