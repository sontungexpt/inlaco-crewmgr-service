package com.inlaco.crewmgrservice.feature.shipschedule.application.service;

import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewUseCase;
import com.inlaco.crewmgrservice.feature.crew.presentation.mapper.CrewProfileMapper;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.ShipScheduleUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.ShipScheduleRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShipScheduleService implements ShipScheduleUseCase {

  private final ShipScheduleRepository shipScheduleRepository;
  private final CrewUseCase crewUseCase;
  private final CrewProfileMapper crewProfileMapper;

  @Override
  public ShipSchedule createSchedule(ShipSchedule schedule) {
    throw new UnsupportedOperationException();
  }
}
