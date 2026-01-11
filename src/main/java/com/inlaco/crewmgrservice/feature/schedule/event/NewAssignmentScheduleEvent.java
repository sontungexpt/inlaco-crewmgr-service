package com.inlaco.crewmgrservice.feature.schedule.event;

import com.inlaco.crewmgrservice.feature.schedule.model.AssignedMobilization;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewAssignmentScheduleEvent extends ApplicationEvent {

  private final AssignedMobilization assigmentSchedule;

  public NewAssignmentScheduleEvent(Object source, AssignedMobilization assigmentSchedule) {
    super(source);
    this.assigmentSchedule = assigmentSchedule;
  }
}
