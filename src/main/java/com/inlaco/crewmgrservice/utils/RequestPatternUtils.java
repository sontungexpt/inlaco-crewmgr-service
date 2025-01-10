package com.inlaco.crewmgrservice.utils;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Slf4j
public class RequestPatternUtils {

  public static boolean isPathVariablePattern(@NonNull String path) {
    boolean uriVar = false;
    for (int i = 0; i < path.length(); i++) {
      char c = path.charAt(i);
      if (c == '{') {
        uriVar = true;
      } else if (c == '}' && uriVar) {
        return true;
      }
    }
    return false;
  }

  public static boolean matchPattern(
      String pattern, RequestMappingHandlerMapping req2HandlerMapping, HttpServletRequest request) {
    try {
      HandlerExecutionChain handlerExeChain = req2HandlerMapping.getHandler(request);
      if (Objects.nonNull(handlerExeChain)) {
        HandlerMethod handlerMethod = (HandlerMethod) handlerExeChain.getHandler();
        Method method = handlerMethod.getMethod();
        return pattern.equals(method.getName());
      }
    } catch (Exception e) {
    }
    return false;
  }

  public static String getPattern(
      RequestMappingHandlerMapping req2HandlerMapping, HttpServletRequest request) {
    try {
      HandlerExecutionChain handlerExeChain = req2HandlerMapping.getHandler(request);
      if (Objects.nonNull(handlerExeChain)) {
        HandlerMethod handlerMethod = (HandlerMethod) handlerExeChain.getHandler();
        return handlerMethod.getMethod().getName();
      }
    } catch (Exception e) {
    }
    return null;
  }
}
