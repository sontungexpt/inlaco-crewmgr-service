package com.inlaco.crewmgrservice.feature.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.contract.model.LaborContract;
import com.inlaco.crewmgrservice.feature.user.dto.BasicProfileDTO;
import com.inlaco.crewmgrservice.feature.user.dto.SailorFilterable;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import com.inlaco.crewmgrservice.feature.user.model.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SailorService {

  void makeSailorOfficial(LaborContract contract);

  Page<BasicProfileDTO> getAllSailors(SailorFilterable filterable, Pageable pageable);

  Page<BasicProfileDTO> searchSailors(String query, SailorFilterable filterable, Pageable pageable);

  SailorProfile updateSailorProfile(String sailorId, JsonNode patch);

  SailorProfile saveSailorProfile(SailorProfile sailorProfile);

  String generateSailorCardId();

  SailorProfile addSailor(String candidateId, SailorProfile profile);

  /**
   * Find sailor profile by id. For user that has role ADMIN, they can view all sailor profiles.
   *
   * @param sailorId the sailor id
   * @return the sailor profile
   */
  SailorProfile findSailorProfileById(String sailorId);

  SailorProfile findSailorProfileByAccountId(String accountId);

  /**
   * Find current sailor profile of user.
   *
   * @param user the user
   * @return the sailor profile
   */
  SailorProfile findMySailorProfile(User user);

  boolean existsSailorProfileById(String sailorId);

  List<SailorProfile> findSailorProfilesByCardIds(Iterable<String> sailorCardIds);

  SailorProfile findSailorProfileByCardId(String cardId);
}
