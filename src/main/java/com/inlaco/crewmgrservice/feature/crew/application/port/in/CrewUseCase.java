package com.inlaco.crewmgrservice.feature.crew.application.port.in;

import com.inlaco.crewmgrservice.feature.crew.application.model.CrewProfileSearchCriteria;
import com.inlaco.crewmgrservice.feature.crew.domain.model.ApplyLaborContractCommand;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
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
}
