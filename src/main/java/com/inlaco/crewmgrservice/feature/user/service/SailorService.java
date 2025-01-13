package com.inlaco.crewmgrservice.feature.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.user.dto.BasicProfileDTO;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import com.inlaco.crewmgrservice.feature.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SailorService {

  Page<BasicProfileDTO> getAllSailors(Pageable pageable);

  Page<BasicProfileDTO> searchSailors(String query, String sailorPositionId, Pageable pageable);

  SailorProfile updateSailorProfile(String sailorId, JsonNode patch);

  String generateSailorCardId();

  SailorProfile addSailor(String candidateId, SailorProfile profile);

  /**
   * Find sailor profile by id. For user that has role ADMIN, they can view all sailor profiles.
   *
   * @param sailorId the sailor id
   * @return the sailor profile
   */
  SailorProfile findSailorProfileById(String sailorId);

  /**
   * Find current sailor profile of user.
   *
   * @param user the user
   * @return the sailor profile
   */
  SailorProfile findMySailorProfile(User user);
}
