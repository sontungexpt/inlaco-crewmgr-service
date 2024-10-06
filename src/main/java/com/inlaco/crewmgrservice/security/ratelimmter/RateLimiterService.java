// package com.foodey.server.security.ratelimmter;

// import com.foodey.server.utils.HttpServletRequestUtils;
// import jakarta.servlet.http.HttpServletRequest;
// import lombok.RequiredArgsConstructor;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Service;
// import org.springframework.util.StringUtils;

// @Service
// @RequiredArgsConstructor
// public class RateLimiterService {

//   public String username() {
//     String name = SecurityContextHolder.getContext().getAuthentication().getName();
//     return name.equals("anonymousUser") ? null : name;
//   }

//   public String getRemoteAddress() {
//     HttpServletRequest request =
//         HttpServletRequestUtils.getRequest()
//             .orElseThrow(() -> new RuntimeException("Request not found"));
//     return request.getRemoteAddr();
//   }

//   public String getClientIP() {
//     HttpServletRequest request =
//         HttpServletRequestUtils.getRequest()
//             .orElseThrow(() -> new RuntimeException("Request not found"));

//     String ipAddress = request.getHeader("X-Forwarded-For");
//     return StringUtils.hasText(ipAddress)
//         ? ipAddress.split(",")[0].trim()
//         : request.getRemoteAddr();
//   }
// }
