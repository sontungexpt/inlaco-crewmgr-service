package com.inlaco.crewmgrservice.feature.crew.application.service;

import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewIdentityUseCase;
import com.inlaco.crewmgrservice.shared.application.port.out.SequenceGenerator;
import java.time.Year;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrewIdentityService implements CrewIdentityUseCase {

  private final SequenceGenerator sequenceGenerator;
  private final String PREFIX = "employee_card_id_";

  @Override
  public String generateEmployeeCardId() {
    log.debug("Generating employee card ID");
    String year = String.valueOf(Year.now().getValue());
    long seq = sequenceGenerator.next(PREFIX + year);
    String employeeCardId = year + String.format("%05d", seq);
    log.info("Generated employee card ID: {}", employeeCardId);
    return employeeCardId;
  }
}
