package com.inlaco.crewmgrservice.feature.crew.application.port.in;

import com.inlaco.crewmgrservice.feature.crew.application.model.CrewProfileSearchCriteria;
import com.inlaco.crewmgrservice.feature.crew.application.model.UpdateCrewProfileAdminCommand;
import com.inlaco.crewmgrservice.feature.crew.application.model.UpdateCrewProfileCrewCommand;
import com.inlaco.crewmgrservice.feature.crew.domain.model.ApplyLaborContractCommand;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrewUseCase {

  CrewProfile getProfile(String profileId);

  CrewProfile getProfileForAccount(String accountId);

  Page<CrewProfile> getProfiles(CrewProfileSearchCriteria criteria, Pageable pageable);

  List<CrewProfile> getProfilesByEmployeeCardIds(Iterable<String> cardIds);

  void applyLaborContract(ApplyLaborContractCommand command);

  boolean existsAllByEmployeeCardIds(Iterable<String> employeeIds);

  CrewProfile adminUpdateProfile(String id, UpdateCrewProfileAdminCommand command, User user);

  CrewProfile crewUpdateProfile(String id, UpdateCrewProfileCrewCommand command, User user);

  /** Assign crew to a mobilization (before start date) */
  void assignCrew(String profileId);

  /** Mark crew as actively working */
  void engageCrew(String profileId);

  /** Release crew after mobilization ends */
  void releaseCrew(String profileId);

  /** Put crew on leave */
  void putOnLeave(String profileId);

  /** Deactivate crew */
  void deactivateCrew(String profileId);
}
