package com.inlaco.crewmgrservice.feature.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.annotation.CurrentUser;
import com.inlaco.crewmgrservice.annotation.PageableQueryParams;
import com.inlaco.crewmgrservice.config.OpenApiConfig;
import com.inlaco.crewmgrservice.feature.user.dto.BasicProfileDTO;
import com.inlaco.crewmgrservice.feature.user.dto.SailorFilterable;
import com.inlaco.crewmgrservice.feature.user.enums.WorkStatus;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.service.SailorService;
import com.inlaco.crewmgrservice.validation.annotation.ObjectId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sailors")
@Tag(name = "Sailor", description = "Sailor management")
public class SailorController {

  private final SailorService sailorService;

  @Operation(
      summary = "Find sailor profile by id",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
This API is used to get the sailor profile by id.

**Usecase**:
- UC_admin-xem-thong-tin-chi-tiet-thuyen-vien.

""")
  @GetMapping("/{id}")
  @RolesAllowed("ADMIN")
  public SailorProfile findSailorById(@ObjectId @PathVariable("id") String sailorId) {
    return sailorService.findSailorProfileById(sailorId);
  }

  @Operation(
      summary = "Find sailor profile by id",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
This API is used to get the sailor profile by id.

**Usecase**:
- UC_admin-xem-thong-tin-chi-tiet-thuyen-vien.

""")
  @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
  @RolesAllowed("ADMIN")
  public SailorProfile updateSailorProfile(
      @PathVariable("id") @ObjectId String sailorId, @RequestBody JsonNode patch) {
    return sailorService.updateSailorProfile(sailorId, patch);
  }

  @Operation(
      summary = "Fetch all sailor profiles",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
This API is used to get all sailor profiles.

**Usecase**:
- UC_admin-xem-thong-tin-chi-tiet-thuyen-vien.

**NOTE**:
- This API is only accessible by the admin.
- The response is paginated.
- The default page size is 20.

""")
  @GetMapping("")
  @RolesAllowed("ADMIN")
  @PageableQueryParams
  public Page<BasicProfileDTO> getAllSailors(
      @RequestParam(required = false) String professionalPosition,
      @RequestParam(required = false) WorkStatus workStatus,
      @RequestParam(required = false) Boolean official,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return sailorService.getAllSailors(
        SailorFilterable.builder()
            .workStatus(workStatus)
            .official(official)
            .professionalPosition(professionalPosition)
            .build(),
        pageable);
  }

  @Operation(
      summary = "Search sailors",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
This API is is used to search sailors by name, email, phone number, accountId.

Can be filtered by position

**Usecase**:
- UC_admin-tim-kiem-thuyen-vien.

**NOTE**:
- This API is only accessible by the admin.
- The response is paginated.
- The default page size is 20.

""")
  @GetMapping("/search")
  @RolesAllowed("ADMIN")
  @PageableQueryParams
  public Page<BasicProfileDTO> searchSailors(
      @RequestParam String q,
      @RequestParam(required = false) String professionalPosition,
      @RequestParam(required = false) WorkStatus workStatus,
      @RequestParam(required = false) Boolean official,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return sailorService.searchSailors(
        q,
        SailorFilterable.builder()
            .workStatus(workStatus)
            .official(official)
            .professionalPosition(professionalPosition)
            .build(),
        pageable);
  }

  @Operation(
      summary = "Find sailor profile of current user",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
This API is used to get the sailor profile of the current user.

**Usecase**:
- UC_admin-xem-thong-tin-chi-tiet-thuyen-vien.

""")
  @GetMapping("/profile/me")
  @RolesAllowed("SAILOR")
  public SailorProfile findMySailorProfile(@CurrentUser User sailor) {
    return sailorService.findMySailorProfile(sailor);
  }

  @Operation(
      summary = "Find sailor profile of user with account id",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
This API is used to get the sailor profile of the user with account id.

**Usecase**:
- UC_admin-xem-thong-tin-chi-tiet-thuyen-vien.

**NOTE**:
- This API is only accessible by the admin.

""")
  @GetMapping("/users/{id}")
  @RolesAllowed("ADMIN")
  public SailorProfile findSailorProfileByAccountId(
      @PathVariable("id") @ObjectId String accountId) {
    return sailorService.findSailorProfileById(accountId);
  }

  @Operation(
      summary = "Add new sailor",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
This API is used to add a new sailor from an candidate profile.
It just create and save the profile of the sailor but
does not grant permissions to the sailor because the sailor does not have any contract.

Please create a contract for the sailor to grant permissions.
And sign the contract to activate the permissions.

**Usecase**:
- UC_admin-them-thuyen-vien-moi.

""")
  @PostMapping("/{candidateId}")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.CREATED)
  public SailorProfile createSailor(
      @PathVariable("candidateId") @ObjectId String candidateId,
      @Valid @RequestBody SailorProfile profile) {
    return sailorService.addSailor(candidateId, profile);
  }
}
