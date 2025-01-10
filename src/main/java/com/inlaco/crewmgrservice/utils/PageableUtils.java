package com.inlaco.crewmgrservice.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtils {

  public static Pageable extendDefaultSort(Pageable pageable) {
    Sort sort = pageable.getSort().and(Sort.by(Sort.Order.asc("createdAt"), Sort.Order.asc("id")));
    return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
  }

  public static Pageable extendDefaultSort(
      Pageable pageable, String idFieldName, String createdAtFieldName) {
    Sort sort =
        pageable
            .getSort()
            .and(Sort.by(Sort.Order.asc(createdAtFieldName), Sort.Order.asc(idFieldName)));
    return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
  }
}
