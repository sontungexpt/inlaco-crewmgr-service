package com.inlaco.crewmgrservice.feature.user.service;

import com.inlaco.crewmgrservice.feature.user.dto.BasicProfileDTO;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CandidateService {

  Page<BasicProfileDTO> getAllCandidates(Pageable pageable);

  CandidateProfile getProfileByCandidateId(String candidateId);
}
