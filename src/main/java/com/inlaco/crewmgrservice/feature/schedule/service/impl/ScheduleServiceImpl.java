package com.inlaco.crewmgrservice.feature.schedule.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.notify.NotificationFactory;
import com.inlaco.crewmgrservice.feature.notify.NotificationType;
import com.inlaco.crewmgrservice.feature.notify.mail.EmailRequest;
import com.inlaco.crewmgrservice.feature.schedule.dto.SailorScheduleResponse;
import com.inlaco.crewmgrservice.feature.schedule.dto.ScheduleFilterable;
import com.inlaco.crewmgrservice.feature.schedule.dto.ScheduleResponse;
import com.inlaco.crewmgrservice.feature.schedule.event.NewAssignmentScheduleEvent;
import com.inlaco.crewmgrservice.feature.schedule.model.AssigmentSchedule;
import com.inlaco.crewmgrservice.feature.schedule.repository.AssignmentScheduleRepository;
import com.inlaco.crewmgrservice.feature.schedule.repository.CustomScheduleRepository;
import com.inlaco.crewmgrservice.feature.schedule.service.ScheduleService;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import com.inlaco.crewmgrservice.feature.user.service.SailorService;
import com.inlaco.crewmgrservice.utils.JsonMergePatchUtils;
import com.inlaco.crewmgrservice.utils.TextTemplateBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

  @Value("${inlaco.client.base-url}")
  private String CLIENT_HOME_PAGE_LINK;

  private String SAILOR_WORK_EMAIL_NOTIFICATION_PATH =
      "src/main/resources/templates/email/html/schedule/sailor-work-notification.html";

  private final AssignmentScheduleRepository scheduleRepository;
  private final CustomScheduleRepository customScheduleRepository;
  private final JsonMergePatchUtils jsonMergePatch;
  private final ApplicationEventPublisher eventPublisher;
  private final SailorService sailorService;
  private final NotificationFactory notificationFactory;

  @Override
  public AssigmentSchedule createSchedule(AssigmentSchedule schedule) {
    var newSchedule = scheduleRepository.save(schedule);
    eventPublisher.publishEvent(new NewAssignmentScheduleEvent(this, schedule));
    log.info("New schedule created: {}", newSchedule);
    notifySailorSchedule(newSchedule);
    return newSchedule;
  }

  public void notifySailorSchedule(AssigmentSchedule schedule) {
    List<String> cardIds = schedule.getCrewMembers().stream().map(it -> it.getCardId()).toList();
    List<SailorProfile> profiles = sailorService.findSailorProfilesByCardIds(cardIds);

    try {
      String html = Files.readString(Paths.get(SAILOR_WORK_EMAIL_NOTIFICATION_PATH));
      profiles.forEach(
          profile -> {
            EmailRequest emailRequest =
                EmailRequest.builder(
                        profile.getEmail(),
                        TextTemplateBuilder.content(html)
                            .var("recipient_name", profile.getFullName())
                            .var("company_name", "Inlaco")
                            .var("start_date", schedule.getStartDate().toString())
                            .var("estimated_end_date", schedule.getEndDate().toString())
                            .var("home_page_link", CLIENT_HOME_PAGE_LINK)
                            .var("info_link", "")
                            .buildContent(),
                        "Inlaco Work Schedule Notification")
                    .htmlMessage()
                    .build();
            log.info("Notify sailor: {}", profile);
            notificationFactory.sendNotificationAsync(NotificationType.EMAIL, emailRequest);
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Page<AssigmentSchedule> findPaginationSchedules(
      ScheduleFilterable filterable, Pageable pageable) {
    return customScheduleRepository.findAllSchedules(filterable, pageable);
  }

  @Override
  public List<AssigmentSchedule> findSchedules(ScheduleFilterable filterable) {
    return customScheduleRepository.findAllSchedules(filterable);
  }

  @Override
  public Page<SailorScheduleResponse> findPaginationSchedulesByCardId(
      String cardId, ScheduleFilterable filterable, Pageable pageable) {
    var profile = sailorService.findSailorProfileByCardId(cardId);
    return customScheduleRepository
        .findPaginationSchedulesByCardId(cardId, filterable, pageable)
        .map(it -> toSailorScheduleResponse(it, cardId, profile));
  }

  private SailorScheduleResponse toSailorScheduleResponse(
      AssigmentSchedule schedule, String cardId, SailorProfile profile) {
    return SailorScheduleResponse.builder()
        .startDate(schedule.getStartDate())
        .endDate(schedule.getEndDate())
        .professionalPosition(profile.getProfessionalPosition())
        // schedule.getCrewMembers().stream()
        //     .filter(crew -> crew.getCardId().equals(cardId))
        //     .toList()
        //     .get(0)
        //     .getProfessionalPosition())
        .cardId(cardId)
        .detail(schedule)
        .build();
  }

  @Override
  public List<SailorScheduleResponse> findSchedulesByCardId(
      String cardId, ScheduleFilterable filterable) {
    var profile = sailorService.findSailorProfileByCardId(cardId);
    return customScheduleRepository.findSchedulesByCardId(cardId, filterable).stream()
        .map(it -> toSailorScheduleResponse(it, cardId, profile))
        .toList();
  }

  @Override
  public ScheduleResponse findDetailScheduleById(String id) {
    return customScheduleRepository.findDetailSchedule(id);
  }

  @Override
  public ScheduleResponse updateSchedule(String id, JsonNode patch) {
    jsonMergePatch.patch(id, AssigmentSchedule.class, patch);
    return customScheduleRepository.findDetailSchedule(id);
  }
}
