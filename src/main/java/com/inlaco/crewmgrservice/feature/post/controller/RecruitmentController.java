package com.inlaco.crewmgrservice.feature.post.controller;

import com.inlaco.crewmgrservice.feature.post.service.RecruitmentPostService;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recruitments")
@RequiredArgsConstructor
@Tag(name = "Recruitments", description = "A collection endpoints to work with recruitments")
public class RecruitmentController {

  private final RecruitmentPostService recruitmentPostService;

  @PostMapping("/registration-status/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void changePostRegistrationStatus(
      @CurrentUser User user,
      @RequestParam boolean active,
      @RequestParam(required = false) Instant reopenUntil,
      @PathVariable("id") String id) {
    recruitmentPostService.changeRegistrationStatus(id, active, reopenUntil, user);
  }
}
