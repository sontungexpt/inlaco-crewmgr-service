package com.inlaco.crewmgrservice.feature.post.application.port.in;

import com.inlaco.crewmgrservice.feature.user.model.User;
import java.time.Instant;

public interface RecruitmentPostUseCase {

  void changeRegistrationStatus(String postId, boolean active, Instant reopenUntil, User user);
}
