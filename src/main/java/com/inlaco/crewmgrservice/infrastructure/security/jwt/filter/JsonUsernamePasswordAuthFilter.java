// package com.inlaco.crewmgrservice.infrastructure.security.jwt.filter;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.inlaco.crewmgrservice.feature.auth.presentation.dto.request.LoginRequest;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import java.io.IOException;
// import lombok.RequiredArgsConstructor;
// import org.springframework.security.authentication.AuthenticationServiceException;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @RequiredArgsConstructor
// public class JsonUsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {

//   private final ObjectMapper objectMapper;

//   @Override
//   public Authentication attemptAuthentication(
//       HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//     if (!request.getMethod().equals("POST")) {
//       throw new AuthenticationServiceException(
//           "Authentication method not supported: " + request.getMethod());
//     }

//     try {
//       LoginRequest body = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
//       UsernamePasswordAuthenticationToken authRequest =
//           new UsernamePasswordAuthenticationToken(body.username(), body.password());

//       setDetails(request, authRequest);
//       return this.getAuthenticationManager().authenticate(authRequest);
//     } catch (IOException e) {
//       throw new AuthenticationServiceException(e.getMessage(), e);
//     }
//   }
// }
