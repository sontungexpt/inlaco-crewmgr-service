package com.inlaco.crewmgrservice.feature.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.annotation.CurrentUser;
import com.inlaco.crewmgrservice.annotation.PageableQueryParams;
import com.inlaco.crewmgrservice.config.OpenApiConfig;
import com.inlaco.crewmgrservice.feature.user.dto.BasicProfileDTO;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.service.CandidateService;
import com.inlaco.crewmgrservice.validation.annotation.ObjectId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/candidates")
@Tag(name = "Candidate", description = "Candidate API")
public class CandidateController {

  private final CandidateService candidateService;

  @Operation(
      summary = "Apply for a job",
      description =
"""
Apply for a job with the given job id.

**Usecase**:

- UC_general-user-nop-ho-so-ung-tuyen
""",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("/recruitment/{postId}")
  @ResponseStatus(HttpStatus.CREATED)
  @RolesAllowed("USER")
  public CandidateProfile applyCandidate(
      @ObjectId @PathVariable("postId") String postId,
      @RequestParam String resumeAssetId,
      @CurrentUser User user,
      @RequestBody @Valid CandidateProfile candidateProfile) {
    return candidateService.applyCandidate(postId, candidateProfile, resumeAssetId, user);
  }

  @Operation(
      summary = "Update a candidate profile",
      description =
"""
Update a candidate profile with the given id.

**Usecase**:

- UC_general-user-cap-nhat-thong-tin-ung-vien
""",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
  @ResponseStatus(HttpStatus.OK)
  @RolesAllowed("USER")
  public CandidateProfile updateCandidateProfile(
      @CurrentUser User user,
      @ObjectId @PathVariable("id") String id,
      @RequestBody JsonNode patch) {
    return candidateService.updateCandidateProfile(id, patch, user);
  }

  @Operation(
      summary = "Retrieve all candidates profiles",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
"""
This API retrieves a list of candidate profiles from the server.

**Use cases:**
- UC_admin-xem-ho-so-ung-tuyen-thuyen-vien.

**Notes:**
- Pagination is required.
- Sorting is optional but can be applied.

""")
  @GetMapping("/web")
  @ResponseStatus(HttpStatus.OK)
  @PageableQueryParams
  @RolesAllowed("ADMIN")
  public Page<?> getAllCandidates(
      @RequestParam(required = false) String recruitmentPostId,
      @RequestParam(defaultValue = "APPLIED") CandidateProfile.Status status,
      @PageableDefault(size = 10, page = 0) Pageable pageable) {
    return candidateService.getAllCandidates(recruitmentPostId, status, pageable);
  }

  @Operation(
      summary = "Search candidates profiles by candidate name",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
"""
This API retrieves a list of candidate profiles from the server based on the candidate name.

If you want to filter by some fields, you can pass the filter object as a JSON object in the request body.

**Use cases:**
- UC_admin-xem-ho-so-ung-tuyen-thuyen-vien.

**Notes:**
- Pagination is required.
- Sorting is optional but can be applied.

""")
  @GetMapping("/search")
  @ResponseStatus(HttpStatus.OK)
  @PageableQueryParams
  @RolesAllowed("ADMIN")
  public Page<BasicProfileDTO> searchCandidates(
      @RequestParam String q,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              content =
                  @io.swagger.v3.oas.annotations.media.Content(
                      schema =
                          @io.swagger.v3.oas.annotations.media.Schema(
                              type = "object",
                              example = "{\"status\": \"APPLIED\"}")))
          @RequestBody(required = false)
          Map<String, Object> filters,
      @PageableDefault(size = 10, page = 0) Pageable pageable) {
    return candidateService.searchCandidates(q, filters, pageable);
  }

  @Operation(
      summary = "Retrieve a detail candidate profile from the server by id",
      description =
"""
This API retrieves a detail candidate profile from the server based on its id.

**Use cases:**
- UC_admin-xem-ho-so-ung-tuyen-thuyen-vien.

""")
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @RolesAllowed("USER")
  public CandidateProfile getCandidateProfile(@ObjectId @PathVariable("id") String id) {
    return candidateService.getCandidateProfileById(id);
  }

  @Operation(
      summary = "Admin review candidate profile",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
"""
Admin review candidate profile based on its id.

**Use cases:**
- UC_admin-xet-duyet-ho-so-ung-tuyen-thuyen-vien.

**Notes:**
- Admin can choose to auto send an email to the candidate or not.

""")
  @PostMapping("/review/{candidateId}")
  @ResponseStatus(HttpStatus.OK)
  @RolesAllowed("ADMIN")
  public void adminReviewCandidate(
      @RequestParam(defaultValue = "true") boolean autoEmail,
      @RequestParam CandidateProfile.Status status,
      @ObjectId @PathVariable("candidateId") String id) {
    candidateService.reviewCandidate(id, status, autoEmail);
  }

  @Operation(
      summary = "Get candidate profile of current user",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
"""
This API retrieves the candidate profile of the current user.

**Use cases:**
- UC_crew-xem-thong-tin-tai-khoan.

""")
  @GetMapping("/profile/me")
  @RolesAllowed("SAILOR")
  public List<CandidateProfile> getMyCandidateProfile(@CurrentUser User user) {
    return candidateService.getMyCandidateProfile(user);
  }
}
