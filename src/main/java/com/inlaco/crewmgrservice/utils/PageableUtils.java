package com.inlaco.crewmgrservice.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class PageableUtils {

  public static Pageable extendDefaultSort(Pageable pageable) {
    return extendSort(pageable, Order.desc("updatedAt"), Order.desc("createdAt"), Order.asc("id"));
  }

  public static Pageable extendSort(Pageable pageable, Order... orders) {
    Sort sort = pageable.getSort().and(Sort.by(orders));
    return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
  }
}
