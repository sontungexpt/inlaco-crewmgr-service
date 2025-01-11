package com.inlaco.crewmgrservice.feature.user.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.user.dto.BasicProfileDTO;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.repository.SailorProfileRepository;
import com.inlaco.crewmgrservice.feature.user.service.SailorService;
import com.inlaco.crewmgrservice.feature.user.service.UserService;
import com.inlaco.crewmgrservice.utils.JsonMergePatchUtils;
import java.time.Year;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SailorServiceImpl implements SailorService {

  private final UserService userService;
  private final SailorProfileRepository sailorProfileRepository;
  private final JsonMergePatchUtils jsonMergePatchUtils;

  @Override
  public User updateToSailor(String userId, SailorProfile profile) {
    User user = userService.findUserById(userId);
    profile.setCardId(generateSailorCardId());
    return user;
  }

  @Override
  public String generateSailorCardId() {
    int currentYear = Year.now().getValue();
    String currentIndexStr = String.format("%05d", sailorProfileRepository.count() + 1);
    return currentYear + currentIndexStr;
  }

  @Override
  public Page<BasicProfileDTO> getAllSailors(Pageable pageable) {
    return null;
  }

  @Override
  public SailorProfile updateProfile(String id, JsonNode updatePatch) {
    return jsonMergePatchUtils.patch(id, SailorProfile.class, updatePatch);
  }

  @Override
  public SailorProfile findSailorProfileById(String sailorId) {
    return sailorProfileRepository
        .findById(sailorId)
        .orElseThrow(() -> new ResourceNotFoundException(SailorProfile.class, "id", sailorId));
  }
}
