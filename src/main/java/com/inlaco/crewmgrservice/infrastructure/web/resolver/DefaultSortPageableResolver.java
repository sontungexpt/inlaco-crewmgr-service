package com.inlaco.crewmgrservice.infrastructure.web.resolver;

import com.inlaco.crewmgrservice.infrastructure.persistence.support.PageableUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Primary
public class DefaultSortPageableResolver extends PageableHandlerMethodArgumentResolver {

  @Override
  public Pageable resolveArgument(
      MethodParameter methodParameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {
    Pageable pageable =
        super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
    // If client sorted then respect
    if (pageable.getSort().isSorted()) {
      return pageable;
    }
    return PageableUtils.enforceIdSort(pageable);
  }
}
