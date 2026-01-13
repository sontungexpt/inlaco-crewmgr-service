package com.inlaco.crewmgrservice.feature.post.service;

import com.inlaco.crewmgrservice.feature.user.model.User;
import java.time.Instant;

public interface RecruitmentPostService {

  void changeRegistrationStatus(String postId, boolean active, Instant reopenUntil, User user);
}
