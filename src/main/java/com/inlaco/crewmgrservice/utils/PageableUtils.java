package com.inlaco.crewmgrservice.utils;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class PageableUtils {

  public static Pageable extendDefaultSort(Pageable pageable) {
    return extendSort(pageable, Order.desc("updatedAt"), Order.desc("createdAt"), Order.asc("id"));
  }

  public static Pageable extendDefaultSort(Pageable pageable, Class<?> collection) {
    List<Order> orders = new ArrayList<>();
    String idField = AnnotationUtils.getFirstAnnotationFieldName(collection, Id.class);
    if (idField != null) {
      orders.add(Order.asc(idField));
    }
    String updatedAtField =
        AnnotationUtils.getFirstAnnotationFieldName(collection, LastModifiedDate.class);
    if (updatedAtField != null) {
      orders.add(Order.desc(updatedAtField));
    }
    String createdAtField =
        AnnotationUtils.getFirstAnnotationFieldName(collection, CreatedDate.class);
    if (createdAtField != null) {
      orders.add(Order.desc(createdAtField));
    }

    return extendSort(pageable, orders);
  }

  public static Pageable extendSort(Pageable pageable, Order... orders) {
    Sort sort = pageable.getSort().and(Sort.by(orders));
    return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
  }

  public static Pageable extendSort(Pageable pageable, List<Order> orders) {
    Sort sort = pageable.getSort().and(Sort.by(orders));
    return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
  }
}
