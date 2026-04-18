package com.inlaco.crewmgrservice.feature.schedule.websocket;

import com.inlaco.crewmgrservice.feature.schedule.domain.model.CrewMobilizationSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ScheduleNotificationController {

  private final SimpMessagingTemplate messagingTemplate;

  /**
   * Notify sailors about the creation of a new schedule.
   *
   * @param schedule The newly created schedule.
   */
  public void notifySailors(CrewMobilizationSchedule schedule) {
    messagingTemplate.convertAndSend("/topic/schedules", schedule);
  }
}
