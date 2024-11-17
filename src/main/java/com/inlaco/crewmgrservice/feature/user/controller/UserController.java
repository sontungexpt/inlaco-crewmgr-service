package com.inlaco.crewmgrservice.feature.user.controller;

import com.inlaco.crewmgrservice.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserRepository userRepository;
}
