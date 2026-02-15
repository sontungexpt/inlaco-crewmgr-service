package com.inlaco.crewmgrservice.infrastructure.web.util;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Slf4j
public final class HttpHeaderUtils {

  private HttpHeaderUtils() {}

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";

  /* ===================== BEARER TOKEN ===================== */

  public static Optional<String> extractBearerToken(HttpServletRequest request) {
    return extractHeader(request, AUTHORIZATION_HEADER)
        .flatMap(HttpHeaderUtils::resolveBearerToken);
  }

  public static Optional<String> extractBearerToken(NativeWebRequest request) {
    return extractHeader(request, AUTHORIZATION_HEADER)
        .flatMap(HttpHeaderUtils::resolveBearerToken);
  }

  public static String extractBearerTokenOrThrow(HttpServletRequest request)
      throws MissingServletRequestPartException {
    return extractBearerToken(request)
        .orElseThrow(
            () ->
                new MissingServletRequestPartException(
                    "Missing or invalid Authorization Bearer token"));
  }

  public static String extractBearerTokenOrThrow(NativeWebRequest request)
      throws MissingServletRequestPartException {
    return extractBearerToken(request)
        .orElseThrow(
            () ->
                new MissingServletRequestPartException(
                    "Missing or invalid Authorization Bearer token"));
  }

  /* ===================== HEADER ===================== */

  public static Optional<String> extractHeader(HttpServletRequest request, String headerName) {
    String value = request.getHeader(headerName);
    return normalizeHeader(headerName, value);
  }

  public static Optional<String> extractHeader(NativeWebRequest request, String headerName) {
    String value = request.getHeader(headerName);
    return normalizeHeader(headerName, value);
  }

  public static String extractHeaderOrThrow(HttpServletRequest request, String headerName)
      throws MissingServletRequestPartException {
    return extractHeader(request, headerName)
        .orElseThrow(() -> new MissingServletRequestPartException("Missing header: " + headerName));
  }

  /* ===================== INTERNAL ===================== */

  private static Optional<String> normalizeHeader(String name, String value) {
    if (!StringUtils.hasText(value)) {
      log.debug("Header {} not present", name);
      return Optional.empty();
    }
    return Optional.of(value.trim());
  }

  private static Optional<String> resolveBearerToken(String headerValue) {
    if (!StringUtils.hasText(headerValue)) return Optional.empty();

    if (!headerValue.regionMatches(true, 0, BEARER_PREFIX, 0, BEARER_PREFIX.length())) {
      log.debug("Authorization header does not start with Bearer");
      return Optional.empty();
    }

    String token = headerValue.substring(BEARER_PREFIX.length()).trim();
    return StringUtils.hasText(token) ? Optional.of(token) : Optional.empty();
  }
}
