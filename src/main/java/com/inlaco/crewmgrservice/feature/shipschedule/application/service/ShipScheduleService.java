package com.inlaco.crewmgrservice.feature.shipschedule.application.service;

import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.ShipScheduleUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.ShipScheduleRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShipScheduleService implements ShipScheduleUseCase {
  
  private final ShipScheduleRepository shipScheduleRepository;
  
  @Override
  public ShipSchedule createSchedule(ShipSchedule schedule) {
    schedule.setCreatedAt(Instant.now());
    schedule.setUpdatedAt(Instant.now());
    return shipScheduleRepository.save(schedule);
  }
  
  @Override
  public ShipSchedule updateSchedule(String id, ShipSchedule schedule) {
    Optional<ShipSchedule> existing = shipScheduleRepository.findById(id);
    if (existing.isEmpty()) {
      throw new IllegalArgumentException("Ship schedule not found with id: " + id);
    }
    
    ShipSchedule existingSchedule = existing.get();
    existingSchedule.setClientId(schedule.getClientId());
    existingSchedule.setShipId(schedule.getShipId());
    existingSchedule.setShipName(schedule.getShipName());
    existingSchedule.setRoute(schedule.getRoute());
    existingSchedule.setDepartureTime(schedule.getDepartureTime());
    existingSchedule.setArrivalTime(schedule.getArrivalTime());
    existingSchedule.setDeparturePort(schedule.getDeparturePort());
    existingSchedule.setArrivalPort(schedule.getArrivalPort());
    existingSchedule.setUpdatedBy(schedule.getUpdatedBy());
    existingSchedule.setUpdatedAt(Instant.now());
    
    if (schedule.getStatus() != null) {
      existingSchedule.changeStatus(schedule.getStatus());
    }
    
    if (schedule.getEmployeeCardIds() != null) {
      existingSchedule.updateCrewList(schedule.getEmployeeCardIds());
    }
    
    return shipScheduleRepository.save(existingSchedule);
  }
  
  @Override
  public void deleteSchedule(String id) {
    if (!shipScheduleRepository.existsById(id)) {
      throw new IllegalArgumentException("Ship schedule not found with id: " + id);
    }
    shipScheduleRepository.deleteById(id);
  }
  
  @Override
  public Optional<ShipSchedule> getScheduleById(String id) {
    return shipScheduleRepository.findById(id);
  }
  
  @Override
  public List<ShipSchedule> getSchedulesByClientId(String clientId) {
    return shipScheduleRepository.findByClientId(clientId);
  }
  
  @Override
  public List<ShipSchedule> getSchedulesByShipId(String shipId) {
    return shipScheduleRepository.findByShipId(shipId);
  }
  
  @Override
  public List<ShipSchedule> getSchedulesByDateRange(Instant startTime, Instant endTime) {
    return shipScheduleRepository.findByDepartureTimeBetween(startTime, endTime);
  }
  
  @Override
  public List<ShipSchedule> getAllSchedules() {
    return shipScheduleRepository.findAll();
  }
  
  @Override
  public ShipSchedule addCrewMember(String scheduleId, String employeeCardId) {
    Optional<ShipSchedule> existing = shipScheduleRepository.findById(scheduleId);
    if (existing.isEmpty()) {
      throw new IllegalArgumentException("Ship schedule not found with id: " + scheduleId);
    }
    
    ShipSchedule schedule = existing.get();
    schedule.addCrewMember(employeeCardId);
    schedule.setUpdatedAt(Instant.now());
    
    return shipScheduleRepository.save(schedule);
  }
  
  @Override
  public ShipSchedule removeCrewMember(String scheduleId, String employeeCardId) {
    Optional<ShipSchedule> existing = shipScheduleRepository.findById(scheduleId);
    if (existing.isEmpty()) {
      throw new IllegalArgumentException("Ship schedule not found with id: " + scheduleId);
    }
    
    ShipSchedule schedule = existing.get();
    schedule.removeCrewMember(employeeCardId);
    schedule.setUpdatedAt(Instant.now());
    
    return shipScheduleRepository.save(schedule);
  }
  
  @Override
  public ShipSchedule updateCrewList(String scheduleId, List<String> employeeCardIds) {
    Optional<ShipSchedule> existing = shipScheduleRepository.findById(scheduleId);
    if (existing.isEmpty()) {
      throw new IllegalArgumentException("Ship schedule not found with id: " + scheduleId);
    }
    
    ShipSchedule schedule = existing.get();
    schedule.updateCrewList(employeeCardIds);
    schedule.setUpdatedAt(Instant.now());
    
    return shipScheduleRepository.save(schedule);
  }
}
