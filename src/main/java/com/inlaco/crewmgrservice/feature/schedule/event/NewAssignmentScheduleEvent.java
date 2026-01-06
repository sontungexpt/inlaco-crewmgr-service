package com.inlaco.crewmgrservice.feature.schedule.event;

import com.inlaco.crewmgrservice.feature.schedule.model.AssigmentSchedule;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewAssignmentScheduleEvent extends ApplicationEvent {

  private final AssigmentSchedule assigmentSchedule;

  public NewAssignmentScheduleEvent(Object source, AssigmentSchedule assigmentSchedule) {
    super(source);
    this.assigmentSchedule = assigmentSchedule;
  }
}
