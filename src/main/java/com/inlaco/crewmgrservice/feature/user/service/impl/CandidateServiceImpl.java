package com.inlaco.crewmgrservice.feature.user.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.notify.NotificationFactory;
import com.inlaco.crewmgrservice.feature.notify.NotificationType;
import com.inlaco.crewmgrservice.feature.notify.mail.EmailRequest;
import com.inlaco.crewmgrservice.feature.notify.mail.EmailType;
import com.inlaco.crewmgrservice.feature.post.enums.PostType;
import com.inlaco.crewmgrservice.feature.post.exception.PostInactiveException;
import com.inlaco.crewmgrservice.feature.post.model.Post;
import com.inlaco.crewmgrservice.feature.post.model.RecruitmentPost;
import com.inlaco.crewmgrservice.feature.post.service.PostService;
import com.inlaco.crewmgrservice.feature.user.dto.BasicProfileDTO;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.repository.CandidateProfileRepository;
import com.inlaco.crewmgrservice.feature.user.repository.CustomCandidateRepository;
import com.inlaco.crewmgrservice.feature.user.service.CandidateService;
import com.inlaco.crewmgrservice.feature.user.service.state.candidate.ReviewServiceFactory;
import com.inlaco.crewmgrservice.utils.JsonMergePatchUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Year;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

  @Value("inlaco.company-name")
  private String COMPANY_NAME;

  private final CandidateProfileRepository candidateProfileRepository;
  private final CustomCandidateRepository customCandidateRepository;
  private final ReviewServiceFactory reviewServiceFactory;
  private final PostService postService;
  private final JsonMergePatchUtils jsonMergePatchUtils;
  private final NotificationFactory notificationFactory;

  private BasicProfileDTO toBasicProfileDTO(CandidateProfile candidateProfile) {
    return BasicProfileDTO.builder()
        .id(candidateProfile.getId())
        .fullName(candidateProfile.getFullName())
        .email(candidateProfile.getEmail())
        .address(candidateProfile.getAddress())
        .file(candidateProfile.getResume())
        .phoneNumber(candidateProfile.getPhoneNumber())
        .gender(candidateProfile.getGender())
        .birthDate(candidateProfile.getBirthDate())
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

  private String sendApplicationSucessfuleEmail(CandidateProfile profile, RecruitmentPost post) {
    try {
      String html =
          Files.readString(
              Paths.get("src/main/resources/templates/email/html/recruitment/applied.html"));

      var content =
          html.replace("${candidate_name}", profile.getFullName())
              .replace("${position_name}", post.getPosition())
              .replace("${company_name}", COMPANY_NAME)
              .replace("${current_year}", String.format("%d", Year.now().getValue()))
              .replace("${contact_email}", "inlaco@gmail.com");

      notificationFactory.sendNotificationAsync(
          NotificationType.EMAIL,
          EmailRequest.builder(
                  profile.getEmail(), content, "Application Successful - " + COMPANY_NAME)
              .emailType(EmailType.MIME)
              .build());

    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public CandidateProfile applyCandidate(
      String postId, CandidateProfile candidateProfile, User user) {
    Post post = postService.getPost(postId);

    if (post.getType() != PostType.RECRUITMENT) {
      throw new ResourceNotFoundException(
          Post.class, Map.of("id", postId, "type", PostType.RECRUITMENT));
    } else if (!post.isActive()) {
      throw new PostInactiveException("The registration post is closed");
    }

    candidateProfile.setRecruitmentPostId(new ObjectId(postId));
    candidateProfile.setAccountId(new ObjectId(user.getId()));

    var savedProfile = candidateProfileRepository.save(candidateProfile);

    sendApplicationSucessfuleEmail(savedProfile, (RecruitmentPost) post);

    return savedProfile;
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
  public CandidateProfile getMyCandidateProfile(User user) {
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
