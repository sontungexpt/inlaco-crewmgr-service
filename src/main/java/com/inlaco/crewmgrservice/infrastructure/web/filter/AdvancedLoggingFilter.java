package com.inlaco.crewmgrservice.infrastructure.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdvancedLoggingFilter extends OncePerRequestFilter {

  private final ObjectMapper mapper;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (!log.isDebugEnabled()) {
      filterChain.doFilter(request, response);
      return;
    }

    long start = System.currentTimeMillis();

    ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request, 8192);

    ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

    filterChain.doFilter(wrappedRequest, wrappedResponse);
    logRequestResponse(wrappedRequest, wrappedResponse, start);
    wrappedResponse.copyBodyToResponse();
  }

  private void logRequestResponse(
      ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, long start) {

    try {
      long duration = System.currentTimeMillis() - start;

      String requestBody = getRequestBody(request);
      String responseBody = getResponseBody(response);

      StringBuilder logMessage = new StringBuilder();

      logMessage
          .append("\n\n========== HTTP LOG ==========\n")
          .append("[METHOD]: ")
          .append(request.getMethod())
          .append("\n")
          .append("[URI]: ")
          .append(request.getRequestURI())
          .append("\n")
          .append("[QUERY]: ")
          .append(request.getQueryString())
          .append("\n")
          .append("[STATUS]: ")
          .append(response.getStatus())
          .append("\n")
          .append("[DURATION]: ")
          .append(duration)
          .append(" ms\n")
          .append("[TIME]: ")
          .append(Instant.now())
          .append("\n")
          .append("\n[HEADERS]:\n");

      Collections.list(request.getHeaderNames())
          .forEach(
              header ->
                  logMessage
                      .append("--- ")
                      .append(header)
                      .append(" : ")
                      .append(maskIfSensitive(header, request.getHeader(header)))
                      .append("\n"));

      logMessage.append("\n[REQUEST BODY]:\n").append(requestBody);

      logMessage.append("\n\n[RESPONSE BODY]:\n").append(responseBody);

      logMessage.append("\n========== END HTTP LOG ==========\n");

      log.debug(logMessage.toString());

    } catch (Exception e) {
      log.error("Logging error", e);
    }
  }

  private String getRequestBody(ContentCachingRequestWrapper request) {
    byte[] content = request.getContentAsByteArray();
    if (content.length == 0) return "<empty>";

    String body = new String(content, StandardCharsets.UTF_8);
    return prettyIfJson(body);
  }

  private String getResponseBody(ContentCachingResponseWrapper response) {
    byte[] content = response.getContentAsByteArray();
    if (content.length == 0) return "<empty>";
    String body = new String(content, StandardCharsets.UTF_8);
    return prettyIfJson(body);
  }

  private String maskIfSensitive(String header, String value) {
    if (header.equalsIgnoreCase("Authorization")) {
      return "***MASKED***";
    }
    return value;
  }

  private String prettyIfJson(String body) {
    Object json = mapper.readValue(body, Object.class);
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
  }
}
