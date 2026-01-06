package com.inlaco.crewmgrservice.feature.post.service;

import com.inlaco.crewmgrservice.feature.user.model.User;

public interface RecruitmentPostService {

  void changeRegistrationStatus(String postId, boolean active, User user);
}
