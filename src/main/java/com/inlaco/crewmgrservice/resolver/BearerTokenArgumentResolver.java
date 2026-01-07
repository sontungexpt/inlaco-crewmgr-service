package com.inlaco.crewmgrservice.resolver;

import com.inlaco.crewmgrservice.annotation.BearerToken;
import com.inlaco.crewmgrservice.utils.HttpHeaderUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class BearerTokenArgumentResolver implements HandlerMethodArgumentResolver {

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

    BearerToken annotation = parameter.getParameterAnnotation(BearerToken.class);

    if (annotation.throwException()) {
      return HttpHeaderUtils.extractBearerTokenOrThrow(webRequest);
    }

    return HttpHeaderUtils.extractBearerToken(webRequest).orElse(null);
  }
}
