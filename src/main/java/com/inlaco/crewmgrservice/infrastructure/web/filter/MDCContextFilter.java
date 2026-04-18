package com.inlaco.crewmgrservice.infrastructure.web.filter;

import com.inlaco.crewmgrservice.infrastructure.web.util.HttpHeaderUtils;
import com.inlaco.crewmgrservice.shared.constant.MDCContextKey;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MDCContextFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {

      // 1. Lấy traceId từ header nếu có (gọi từ service khác)
      String traceId = request.getHeader("X-Trace-Id");

      // 2. Nếu không có thì tự sinh
      if (traceId == null || traceId.isBlank()) {
        traceId = UUID.randomUUID().toString().replace("-", "");
      }

      // Gắn vào MDC (logback sẽ đọc %X{traceId})
      MDC.put(MDCContextKey.TRACE_ID, traceId);

      // ===== CLIENT IP =====
      String ip = HttpHeaderUtils.getClientIp(request);
      // Gắn vào MDC (logback sẽ đọc %X{clientIp})
      MDC.put(MDCContextKey.CLIENT_IP, ip);

      // trả lại cho client / service khác
      response.setHeader("X-Trace-Id", traceId);

      filterChain.doFilter(request, response);
    } finally {
      MDC.remove(MDCContextKey.TRACE_ID);
      MDC.remove(MDCContextKey.CLIENT_IP);
    }
  }
}
