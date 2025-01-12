package com.inlaco.crewmgrservice.feature.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.user.dto.BasicProfileDTO;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import com.inlaco.crewmgrservice.feature.user.model.User;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CandidateService {

  Page<BasicProfileDTO> getAllCandidates(CandidateProfile.Status status, Pageable pageable);

  Page<BasicProfileDTO> searchCandidates(
      String query, Map<String, Object> filters, Pageable pageable);

  CandidateProfile getCandidateProfileById(String candidateId);

  CandidateProfile applyCandidate(String postId, CandidateProfile candidateProfile, User user);

  void reviewCandidate(String id, CandidateProfile.Status status, boolean autoEmail);

  CandidateProfile updateCandidateProfile(String id, JsonNode patch, User user);

  void cancelCandidateProfile(String id, User user);

  CandidateProfile getCandidateProfileOfUser(User user);
}
