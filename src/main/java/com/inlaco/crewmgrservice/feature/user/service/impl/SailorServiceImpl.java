package com.inlaco.crewmgrservice.feature.user.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.user.dto.BasicProfileDTO;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.repository.SailorProfileRepository;
import com.inlaco.crewmgrservice.feature.user.service.CandidateService;
import com.inlaco.crewmgrservice.feature.user.service.SailorService;
import com.inlaco.crewmgrservice.feature.user.service.UserService;
import com.inlaco.crewmgrservice.utils.JsonMergePatchUtils;
import java.time.Year;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SailorServiceImpl implements SailorService {

  private final UserService userService;
  private final SailorProfileRepository sailorProfileRepository;
  private final JsonMergePatchUtils jsonMergePatchUtils;
  private final CandidateService candidateService;

  @Override
  public SailorProfile addSailor(String candidateId, SailorProfile sailorProfile) {
    CandidateProfile candidateProfile = candidateService.getCandidateProfileById(candidateId);

    sailorProfile.setAccountId(candidateProfile.getAccountId());
    sailorProfile.setCandidateId(new ObjectId(candidateId));

    return sailorProfileRepository.save(sailorProfile);
  }

  @Override
  public String generateSailorCardId() {
    String currentYear = String.format("%04d", Year.now().getValue());
    String currentIndex = String.format("%05d", sailorProfileRepository.count() + 1);
    var cardId = currentYear + currentIndex;
    log.debug("Generated sailor card id: {}", cardId);
    return cardId;
  }

  private BasicProfileDTO toBasicProfileDTO(SailorProfile sailorProfile) {
    return BasicProfileDTO.builder()
        .id(sailorProfile.getId())
        .fullName(sailorProfile.getFullName())
        .email(sailorProfile.getEmail())
        .address(sailorProfile.getAddress())
        // .file(sailorProfile.get())
        .phoneNumber(sailorProfile.getPhoneNumber())
        .gender(sailorProfile.getGender())
        .build();
  }

  @Override
  public Page<BasicProfileDTO> getAllSailors(Pageable pageable) {
    return sailorProfileRepository.findAll(pageable).map(it -> toBasicProfileDTO(it));
  }

  @Override
  public SailorProfile findSailorProfileById(String sailorId) {
    log.info("Find sailor profile with id: {}", sailorId);
    return sailorProfileRepository
        .findById(sailorId)
        .orElseThrow(() -> new ResourceNotFoundException(SailorProfile.class, "id", sailorId));
  }

  @Override
  public Page<BasicProfileDTO> searchSailors(
      String query, String sailorPositionId, Pageable pageable) {
    throw new UnsupportedOperationException("Unimplemented method 'searchSailors'");
  }

  @Override
  public SailorProfile updateSailorProfile(String sailorId, JsonNode patch) {
    return jsonMergePatchUtils.patch(sailorId, SailorProfile.class, patch);
  }

  @Override
  public SailorProfile findMySailorProfile(User user) {
    return sailorProfileRepository
        .findByAccountId(user.getId())
        .orElseThrow(
            () -> new ResourceNotFoundException(SailorProfile.class, "accountId", user.getId()));
  }
}
