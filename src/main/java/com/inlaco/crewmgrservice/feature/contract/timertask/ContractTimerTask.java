package com.inlaco.crewmgrservice.feature.contract.timertask;

import static org.springframework.data.mongodb.core.query.Query.query;

import com.inlaco.crewmgrservice.feature.contract.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.model.SupplyContract;
import com.inlaco.crewmgrservice.feature.crewrental.enums.RentalRequestStatus;
import com.inlaco.crewmgrservice.feature.crewrental.model.RentalRequest;
import com.inlaco.crewmgrservice.feature.crewrental.service.RentalRequestService;
import com.inlaco.crewmgrservice.feature.user.enums.WorkStatus;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import com.inlaco.crewmgrservice.feature.user.service.SailorService;
import com.inlaco.crewmgrservice.feature.user.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContractTimerTask {

  private final MongoTemplate mongoTemplate;
  private final RentalRequestService rentalRequestService;
  private final UserService userService;
  private final SailorService sailorService;

  @Scheduled(cron = "0 0/1 * * * ?")
  private void onContractEffective() {
    var query = new Query();
    query.addCriteria(
        Criteria.where("activated").is(false).and("activationDate").lte(Instant.now()));
    log.debug(query.toString());

    List<AbstractContract> contracts = mongoTemplate.find(query, AbstractContract.class);
    contracts.forEach(
        it -> {
          it.setActivated(true);
          handleContractEffective(it);
          mongoTemplate.save(it);
        });
  }

  public void handleContractEffective(Contract contract) {
    CompletableFuture.runAsync(
        () -> {
          log.info("Activating contract {}", contract.getTitle());
          if (contract instanceof SupplyContract that) {
            RentalRequest request =
                rentalRequestService.getRequestById(that.getRentalRequestId().toHexString());
            request.setStatus(RentalRequestStatus.ACTIVE);
            rentalRequestService.saveRequest(request);
          } else if (contract instanceof LaborContract that) {
            SailorProfile sailorProfile =
                sailorService.findSailorProfileByAccountId(that.getEmployeeId().toHexString());
            sailorProfile.setCardId(sailorService.generateSailorCardId());
            sailorProfile.setWorkStatus(WorkStatus.AVAILABLE);
            sailorProfile.setJoinedCompanyAt(Instant.now());
            sailorService.saveSailorProfile(sailorProfile);
            userService.updateToSailor(that.getEmployeeId().toHexString());
          }
          log.info("Contract {} activated", contract.getTitle());
        });
  }
}
