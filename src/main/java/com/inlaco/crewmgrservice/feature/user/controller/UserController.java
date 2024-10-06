package com.inlaco.crewmgrservice.feature.user.controller;

import com.inlaco.crewmgrservice.feature.user.repository.UserRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
// @RolesAllowed(RoleType.Fields.CUSTOMER)
public record UserController(UserRepository userRepository) {}
