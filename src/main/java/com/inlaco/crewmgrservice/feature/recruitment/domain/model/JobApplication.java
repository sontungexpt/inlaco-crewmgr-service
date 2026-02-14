package com.inlaco.crewmgrservice.feature.recruitment.domain.model;

import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import com.inlaco.crewmgrservice.feature.user.domain.enums.Gender;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
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

  private File resume;

  @Setter(AccessLevel.PRIVATE)
  private ApplicationStatus status = ApplicationStatus.APPLIED;

  private Instant appliedAt;

  private Instant updatedAt;

  public void changeStatus(ApplicationStatus newStatus) throws IllegalStateException {
    if (status == newStatus) return;
    if (!status.canTransitionTo(newStatus)) {
      throw new IllegalStateException("Invalid transition from " + status + " to " + newStatus);
    }
    status = newStatus;
  }
}
