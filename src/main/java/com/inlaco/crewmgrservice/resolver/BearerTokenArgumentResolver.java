package com.inlaco.crewmgrservice.resolver;

import com.inlaco.crewmgrservice.annotation.BearerToken;
import com.inlaco.crewmgrservice.utils.HttpHeaderUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Component
public class BearerTokenArgumentResolver implements HandlerMethodArgumentResolver {

  // private static final String AUTHORIZATION_BEARER_PREFIX = "Bearer ";
  // private static final String AUTHORIZATION_HEADER = "Authorization";

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(BearerToken.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      org.springframework.web.bind.support.WebDataBinderFactory binderFactory)
      throws Exception {
    String header = webRequest.getHeader(HttpHeaderUtils.AUTHORIZATION_HEADER);
    BearerToken annotation = parameter.getParameterAnnotation(BearerToken.class);
    if (StringUtils.hasText(header) && header.startsWith(HttpHeaderUtils.BEARER_PREFIX)) {
      return header.substring(HttpHeaderUtils.BEARER_PREFIX.length());
    } else if (annotation.throwException()) {
      throw new MissingServletRequestPartException(HttpHeaderUtils.AUTHORIZATION_HEADER);
    }
    return null;
  }
}
