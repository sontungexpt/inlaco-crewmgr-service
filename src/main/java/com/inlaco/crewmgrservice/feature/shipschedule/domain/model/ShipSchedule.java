package com.inlaco.crewmgrservice.feature.shipschedule.domain.model;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.ScheduleStatus;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipSchedule {
  private String id;
  private String clientId;
  private String shipImo;
  private String shipName;
  private String route;
  private Instant departureTime;
  private Instant arrivalTime;
  private String departurePort;
  private String arrivalPort;
  
  @Builder.Default private ScheduleStatus status = ScheduleStatus.DRAFT;
  
  private List<String> employeeCardIds; // List of crew member employee IDs
  private String createdBy;
  private Instant createdAt;
  private String updatedBy;
  private Instant updatedAt;
  
  public void changeStatus(ScheduleStatus newStatus) throws IllegalStateException {
    if (status == newStatus) return;
    status.validateTransition(newStatus);
    status = newStatus;
  }
  
  public void addCrewMember(String employeeCardId) {
    if (!employeeCardIds.contains(employeeCardId)) {
      employeeCardIds.add(employeeCardId);
    }
  }
  
  public void removeCrewMember(String employeeCardId) {
    employeeCardIds.remove(employeeCardId);
  }
  
  public void updateCrewList(List<String> newEmployeeCardIds) {
    this.employeeCardIds = newEmployeeCardIds;
  }
}
