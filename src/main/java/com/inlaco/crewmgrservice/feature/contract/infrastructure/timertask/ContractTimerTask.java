package com.inlaco.crewmgrservice.feature.contract.infrastructure.timertask;

import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.SupplyContract;
import com.inlaco.crewmgrservice.feature.crewrental.enums.RentalRequestStatus;
import com.inlaco.crewmgrservice.feature.crewrental.model.RentalRequest;
import com.inlaco.crewmgrservice.feature.crewrental.service.RentalRequestService;
import com.inlaco.crewmgrservice.feature.user.service.SailorService;
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
  private final SailorService sailorService;

  @Scheduled(cron = "0 0/1 * * * ?")
  private void onContractEffective() {
    Instant now = Instant.now();
    Criteria criteria = Criteria.where("activated").is(false).and("activationDate").lte(now);
    Query query = Query.query(criteria);

    log.debug("[ContractTimerTask] Activation query: {}", query);

    List<AbstractContract> contracts = mongoTemplate.find(query, AbstractContract.class);

    if (contracts.isEmpty()) {
      log.debug("[ContractTimerTask] No contracts to activate at {}", now);
      return;
    }

    log.info("[ContractTimerTask] Found {} contract(s) to activate", contracts.size());

    contracts.forEach(this::activateContractSafely);
  }

  private void activateContractSafely(AbstractContract contract) {
    CompletableFuture.runAsync(
        () -> {
          try {
            log.info(
                "[ContractTimerTask] Activating contract id={}, title={}",
                contract.getId(),
                contract.getTitle());

            contract.setActivated(true);
            handleContractEffective(contract);
            mongoTemplate.save(contract);

            log.info("[ContractTimerTask] Contract activated successfully id={}", contract.getId());

          } catch (Exception ex) {
            log.error(
                "[ContractTimerTask] Failed to activate contract id={}, title={}",
                contract.getId(),
                contract.getTitle(),
                ex);
          }
        });
  }

  private void handleContractEffective(Contract contract) {
    if (contract instanceof SupplyContract supplyContract) {
      activateSupplyContract(supplyContract);
      return;
    }

    if (contract instanceof LaborContract laborContract) {
      activateLaborContract(laborContract);
      return;
    }

    log.warn(
        "[ContractTimerTask] Unsupported contract type: {} (id={})",
        contract.getClass().getSimpleName());
  }

  private void activateSupplyContract(SupplyContract contract) {
    String requestId = contract.getRentalRequestId().toHexString();

    RentalRequest request = rentalRequestService.getRequestById(requestId);

    if (request == null) {
      log.warn(
          "[ContractTimerTask] RentalRequest not found for SupplyContract id={}", contract.getId());
      return;
    }

    request.setStatus(RentalRequestStatus.ACTIVE);
    rentalRequestService.saveRequest(request);

    log.info(
        "[ContractTimerTask] SupplyContract activated → RentalRequest {} set to ACTIVE", requestId);
  }

  private void activateLaborContract(LaborContract contract) {
    log.info(
        "[ContractTimerTask] Activating LaborContract for employeeId={}", contract.getEmployeeId());

    sailorService.makeSailorOfficial(contract);

    log.info(
        "[ContractTimerTask] LaborContract activated, sailor marked official employeeId={}",
        contract.getEmployeeId());
  }
}
