// package com.inlaco.crewmgrservice.feature.user.application.service;

// import com.inlaco.crewmgrservice.feature.auth.infrastructure.security.SecurityUser;
// import com.inlaco.crewmgrservice.feature.user.application.port.out.UserRepository;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsPasswordService;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;

// @Service
// public record UserDetailsPasswordServiceImpl(
//     UserRepository userRepository, PasswordEncoder passwordEncoder)
//     implements UserDetailsPasswordService {

//   @Override
//   public UserDetails updatePassword(UserDetails userDetails, String newPassword) {
//     SecurityUser securityUser = ((SecurityUser) userDetails);
//     securityUser.user().setPassword(passwordEncoder.encode(newPassword));
//     userRepository.save(securityUser.user());
//     return securityUser;
//   }
// }
