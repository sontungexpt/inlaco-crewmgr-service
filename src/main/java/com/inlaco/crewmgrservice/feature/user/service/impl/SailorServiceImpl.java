package com.inlaco.crewmgrservice.feature.user.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.user.dto.BasicProfileDTO;
import com.inlaco.crewmgrservice.feature.user.dto.SailorFilterable;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.repository.CustomSailorRepository;
import com.inlaco.crewmgrservice.feature.user.repository.SailorProfileRepository;
import com.inlaco.crewmgrservice.feature.user.service.CandidateService;
import com.inlaco.crewmgrservice.feature.user.service.SailorService;
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

  private final SailorProfileRepository sailorProfileRepository;
  private final JsonMergePatchUtils jsonMergePatchUtils;
  private final CandidateService candidateService;
  private final CustomSailorRepository customSailorRepository;

  @Override
  public SailorProfile addSailor(String candidateId, SailorProfile sailorProfile) {
    CandidateProfile candidateProfile = candidateService.getCandidateProfileById(candidateId);

    sailorProfile.setAccountId(candidateProfile.getAccountId());
    sailorProfile.setCandidateId(new ObjectId(candidateId));

    if (sailorProfile.getExperiences() == null || sailorProfile.getExperiences().isEmpty()) {
      sailorProfile.setExperiences(candidateProfile.getExperiences());
    }

    if (sailorProfile.getLanguageSkills() == null || sailorProfile.getLanguageSkills().isEmpty()) {
      sailorProfile.setLanguageSkills(candidateProfile.getLanguageSkills());
    }

    return sailorProfileRepository.save(sailorProfile);
  }

  @Override
  public String generateSailorCardId() {
    String currentYear = String.format("%04d", Year.now().getValue());
    String currentIndex = String.format("%05d", customSailorRepository.countSailorHasCardId() + 1);
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
        .birthDate(sailorProfile.getBirthDate())
        .build();
  }

  @Override
  public Page<BasicProfileDTO> getAllSailors(SailorFilterable filterable, Pageable pageable) {
    return customSailorRepository
        .fetchAllSailors(filterable, pageable)
        .map(this::toBasicProfileDTO);
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
      String query, SailorFilterable filterable, Pageable pageable) {
    return customSailorRepository
        .searchSailors(query, filterable, pageable)
        .map(this::toBasicProfileDTO);
  }

  @Override
  public SailorProfile updateSailorProfile(String sailorId, JsonNode patch) {
    return jsonMergePatchUtils.patch(sailorId, SailorProfile.class, patch);
  }

  @Override
  public SailorProfile findMySailorProfile(User user) {
    return sailorProfileRepository
        .findByAccountId(new ObjectId(user.getId()))
        .orElseThrow(
            () -> new ResourceNotFoundException(SailorProfile.class, "accountId", user.getId()));
  }

  @Override
  public SailorProfile saveSailorProfile(SailorProfile sailorProfile) {
    return sailorProfileRepository.save(sailorProfile);
  }

  @Override
  public SailorProfile findSailorProfileByAccountId(String accountId) {
    return sailorProfileRepository
        .findByAccountId(new ObjectId(accountId))
        .orElseThrow(
            () -> new ResourceNotFoundException(SailorProfile.class, "accountId", accountId));
  }
}
