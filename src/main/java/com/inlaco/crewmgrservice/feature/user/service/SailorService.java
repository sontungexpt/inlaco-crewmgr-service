package com.inlaco.crewmgrservice.feature.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.user.dto.BasicProfileDTO;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import com.inlaco.crewmgrservice.feature.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SailorService {

  String generateSailorCardId();

  User updateToSailor(String userId, SailorProfile profile);

  Page<BasicProfileDTO> getAllSailors(Pageable pageable);

  SailorProfile updateProfile(String id, JsonNode updatePatch);

  SailorProfile findSailorProfileById(String sailorId);
}
