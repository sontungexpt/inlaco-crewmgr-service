package com.inlaco.crewmgrservice.feature.schedule.event.listener;

import com.inlaco.crewmgrservice.feature.schedule.event.NewAssignmentScheduleEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NewAssignmetScheduleEventListener {

  @EventListener(NewAssignmentScheduleEvent.class)
  public void handleNewAssignmentScheduleEvent(NewAssignmentScheduleEvent event) {}
}
