package com.inlaco.crewmgrservice.infrastructure.security.jwt.core;
// package com.inlaco.crewmgrservice.infrastructure.security.jwt.service;

// import com.inlaco.crewmgrservice.shared.objectvalue.CurrentUser;
// import lombok.RequiredArgsConstructor;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Service;

// @Service
// @RequiredArgsConstructor
// public class SpringSecurityCurrentUserProvider implements CurrentUserPort {

//   @Override
//   public CurrentUser getCurrentUser() {
//     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//     // PrincipalUtils.getUser()

//     // if (authentication == null || !authentication.isAuthenticated()) {
//     //     return null;
//     // }

//     // CustomUserPrincipal principal =
//     //     (CustomUserPrincipal) authentication.getPrincipal();

//     // return new CurrentUser(
//     //     principal.getId(),
//     //     principal.getUsername(),
//     //     principal.getEmail(),
//     //     principal.getRoles()
//     // );
//   }
// }
