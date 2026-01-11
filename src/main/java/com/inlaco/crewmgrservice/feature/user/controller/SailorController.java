package com.inlaco.crewmgrservice.feature.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.annotation.CurrentUser;
import com.inlaco.crewmgrservice.annotation.PageableQueryParams;
import com.inlaco.crewmgrservice.config.OpenApiConfig;
import com.inlaco.crewmgrservice.feature.user.dto.BasicProfileDTO;
import com.inlaco.crewmgrservice.feature.user.dto.SailorFilterable;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.service.SailorService;
import com.inlaco.crewmgrservice.validation.annotation.ObjectId;
import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
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
      SailorFilterable filterable, @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return sailorService.getAllSailors(filterable, pageable);
  }

  @Operation(
      summary = "Search sailors",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/search")
  @RolesAllowed("ADMIN")
  @PageableQueryParams
  public Page<SailorProfile> searchSailors(
      @RequestParam String q,
      @Filter Criteria filter,
      SailorFilterable filterable,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return sailorService.searchSailors(q, filter, pageable);
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
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/users/{id}")
  @RolesAllowed("ADMIN")
  public SailorProfile findSailorProfileByAccountId(
      @PathVariable("id") @ObjectId String accountId) {
    return sailorService.findSailorProfileById(accountId);
  }
}
