package com.inlaco.crewmgrservice.feature.recruitment.domain.model;

import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import com.inlaco.crewmgrservice.feature.recruitment.domain.event.ApplicationStatusChangedEvent;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import com.inlaco.crewmgrservice.shared.objectvalue.Gender;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class JobApplication {

  private final transient Set<Object> domainEvents = new HashSet<>();

  protected void registerEvent(Object event) {
    domainEvents.add(event);
  }

  public void broadcast(Consumer<Object> dispatcher) {
    domainEvents.forEach(dispatcher);
    domainEvents.clear();
  }

  private String id;

  private String accountId;

  private String recruitmentPostId;

  private String fullName;

  private String email;

  private String phoneNumber;

  private String address;

  private Gender gender;

  private String languageSkills;

  private String experiences;

  private String position;

  private Asset resume;

  // must use builder here to map all fields without public setter status
  @Default
  @Setter(AccessLevel.PRIVATE)
  private ApplicationStatus status = ApplicationStatus.APPLIED;

  private Instant appliedAt;

  private Instant updatedAt;

  public void changeStatus(ApplicationStatus newStatus) throws IllegalStateException {
    if (status == newStatus) return;
    status.validateTransition(newStatus);
    status = newStatus;
    registerEvent(new ApplicationStatusChangedEvent(this));
  }
}
