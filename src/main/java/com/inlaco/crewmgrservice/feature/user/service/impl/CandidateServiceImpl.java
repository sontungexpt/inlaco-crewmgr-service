package com.inlaco.crewmgrservice.feature.user.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.post.enums.PostType;
import com.inlaco.crewmgrservice.feature.post.model.Post;
import com.inlaco.crewmgrservice.feature.post.service.PostService;
import com.inlaco.crewmgrservice.feature.user.dto.BasicProfileDTO;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.repository.CandidateProfileRepository;
import com.inlaco.crewmgrservice.feature.user.repository.CustomCandidateRepository;
import com.inlaco.crewmgrservice.feature.user.service.CandidateService;
import com.inlaco.crewmgrservice.feature.user.service.state.candidate.ReviewServiceFactory;
import com.inlaco.crewmgrservice.utils.JsonMergePatchUtils;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

  private final CandidateProfileRepository candidateProfileRepository;
  private final CustomCandidateRepository customCandidateRepository;
  private final ReviewServiceFactory reviewServiceFactory;
  private final PostService postService;
  private final JsonMergePatchUtils jsonMergePatchUtils;

  private BasicProfileDTO toBasicProfileDTO(CandidateProfile candidateProfile) {
    return BasicProfileDTO.builder()
        .id(candidateProfile.getId())
        .fullName(candidateProfile.getFullName())
        .email(candidateProfile.getEmail())
        .address(candidateProfile.getAddress())
        .file(candidateProfile.getResume())
        .phoneNumber(candidateProfile.getPhoneNumber())
        .gender(candidateProfile.getGender())
        .build();
  }

  @Override
  public Page<BasicProfileDTO> getAllCandidates(CandidateProfile.Status status, Pageable pageable) {
    return candidateProfileRepository
        .findByStatus(status, pageable)
        .map(it -> toBasicProfileDTO(it));
  }

  @Override
  public CandidateProfile getCandidateProfileById(String id) {
    return candidateProfileRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(CandidateProfile.class, "id", id));
  }

  @Override
  public CandidateProfile applyCandidate(
      String postId, CandidateProfile candidateProfile, User user) {
    Post post = postService.getPost(postId);
    if (post.getType() != PostType.RECRUITMENT) {
      throw new ResourceNotFoundException(
          Post.class, Map.of("id", postId, "type", PostType.RECRUITMENT));
    }

    candidateProfile.setRecruimentPostId(new ObjectId(postId));
    candidateProfile.setAccountId(new ObjectId(user.getId()));

    return candidateProfileRepository.save(candidateProfile);
  }

  @Override
  public void reviewCandidate(String id, CandidateProfile.Status status, boolean autoEmail) {
    reviewServiceFactory.review(status, id, autoEmail);
  }

  @Override
  public CandidateProfile updateCandidateProfile(String id, JsonNode patch, User user) {
    return jsonMergePatchUtils.patch(id, CandidateProfile.class, patch);
  }

  @Override
  public void cancelCandidateProfile(String id, User user) {
    throw new UnsupportedOperationException("Unimplemented method 'cancelCandidateProfile'");
  }

  @Override
  public CandidateProfile getCandidateProfileOfUser(User user) {
    return candidateProfileRepository
        .findByAccountId(new ObjectId(user.getId()))
        .orElseThrow(
            () -> new ResourceNotFoundException(CandidateProfile.class, "accountId", user.getId()));
  }

  @Override
  public Page<BasicProfileDTO> searchCandidates(
      String query, Map<String, Object> filters, Pageable pageable) {
    return customCandidateRepository
        .searchCandidates(query, filters, pageable)
        .map(it -> toBasicProfileDTO(it));
  }
}
