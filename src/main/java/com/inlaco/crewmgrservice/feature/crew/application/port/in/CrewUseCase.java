package com.inlaco.crewmgrservice.feature.crew.application.port.in;

import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.crew.application.model.CrewProfileSearchCriteria;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrewUseCase {

  CrewProfile getProfile(String profileId);

  CrewProfile getProfileForAccount(String accountId);

  Page<CrewProfile> getProfiles(CrewProfileSearchCriteria criteria, Pageable pageable);

  List<CrewProfile> getProfilesByCardIds(Iterable<String> cardIds);

  void makeCrewOfficial(LaborContract contract);

  // SailorProfile updateSailorProfile(String sailorId, JsonNode patch);

  // SailorProfile saveSailorProfile(SailorProfile sailorProfile);

  // SailorProfile findSailorProfileByIdOrNull(String sailorId);

  // SailorProfile findSailorProfileByAccountId(String accountId);

  // SailorProfile findSailorProfileByAccountIdOrNull(String sailorId);

  // SailorProfile findSailorProfileByAccountIdOrNull(ObjectId sailorId);

  // SailorProfile findMySailorProfile(User user);

  // boolean existsSailorProfileById(String sailorId);

  // SailorProfile findSailorProfileByCardId(String cardId);
}
