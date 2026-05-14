package com.inlaco.crewmgrservice.feature.crew.domain.model;

import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewOperationalStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import com.inlaco.crewmgrservice.shared.objectvalue.Gender;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a crew member's profile in the crew management system.
 *
 * <p>Crew profiles contain comprehensive information about maritime crew members including personal
 * details, professional information, and various status indicators that track their operational
 * availability and current assignments.
 *
 * <p>Each crew member has multiple status dimensions: operational status for their overall
 * availability, boarding status for ship assignments, and mobilization status for deployment
 * readiness.
 *
 * @author Trần Võ Sơn Tùng
 * @version 1.0
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrewProfile {

  private String id;

  private String accountId;

  private String fullName;

  private String email;

  private String phoneNumber;

  private Asset image;

  private String address;

  private Gender gender;

  private String professionalPosition;

  private Instant birthDate;

  private String employeeCardId;

  private String citizenIdentityCardId;
  private Asset citizenIdentityCardImageFront;
  private Asset citizenIdentityCardImageBack;

  private String socialInsuranceCode;
  private Asset socialInsuranceImageFront;
  private Asset socialInsuranceImageBack;

  private String accidentInsuranceCode;
  private Asset accidentInsuranceImageFront;
  private Asset accidentInsuranceImageBack;

  @Builder.Default private CrewOperationalStatus status = CrewOperationalStatus.DRAFT;

  /**
   * Changes the crew member's operational status with validation.
   *
   * <p>This method updates the crew member's operational status after validating that the
   * transition is allowed according to business rules defined in the CrewOperationalStatus enum.
   *
   * @param newStatus the new operational status to set
   * @throws IllegalStateException if the status transition is not allowed
   */
  public void changeStatus(CrewOperationalStatus newStatus) throws IllegalStateException {
    if (status == newStatus) return;
    status.validateTransition(newStatus);
    status = newStatus;
  }
}
