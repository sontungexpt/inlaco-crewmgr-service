package com.inlaco.crewmgrservice.infrastructure.persistence.support;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public final class PageableUtils {

  private PageableUtils() {}

  public static Pageable enforceIdSort(Pageable pageable) {
    return extendSort(pageable, Order.desc("id"));
  }

  public static Pageable enforceIdSort(Pageable pageable, Class<?> entityClass) {
    return buildOrder(entityClass, Id.class)
        .map(order -> extendSort(pageable, order.with(Direction.DESC)))
        .orElse(pageable);
  }

  public static Pageable enforceAuditSort(Pageable pageable, Class<?> entityClass) {
    List<Order> orders = new ArrayList<>(2);
    buildOrder(entityClass, LastModifiedDate.class)
        .ifPresent(o -> orders.add(o.with(Direction.DESC)));
    buildOrder(entityClass, CreatedDate.class).ifPresent(o -> orders.add(o.with(Direction.DESC)));
    return extendSort(pageable, orders);
  }

  public static Pageable extendSort(Pageable pageable, Order... orders) {
    return extendSort(pageable, Arrays.asList(orders));
  }

  public static Pageable extendSort(Pageable pageable, Collection<Sort.Order> orders) {
    if (orders == null || orders.isEmpty()) return pageable;
    Sort original = pageable.getSort();
    Set<String> existingProps =
        original.stream().map(Sort.Order::getProperty).collect(Collectors.toSet());
    List<Sort.Order> mergedOrders = new ArrayList<>(original.toList());
    for (Sort.Order order : orders) {
      if (order != null && !existingProps.contains(order.getProperty())) {
        mergedOrders.add(order);
      }
    }
    if (mergedOrders.isEmpty() || mergedOrders.equals(original.toList())) {
      return pageable;
    }

    Sort merged = Sort.by(mergedOrders);
    return pageable.isPaged()
        ? PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), merged)
        : Pageable.unpaged(merged);
  }

  public static Optional<Order> buildOrder(
      Class<?> entityClass, Class<? extends Annotation> annotation) {
    String field = AnnotationUtil.findFirstAnnotationFieldName(entityClass, annotation);
    return Optional.ofNullable(field).map(Order::by);
  }
}
