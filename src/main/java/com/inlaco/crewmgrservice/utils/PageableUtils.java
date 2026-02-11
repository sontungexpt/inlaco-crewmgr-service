package com.inlaco.crewmgrservice.utils;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public final class PageableUtils {

  private PageableUtils() {}

  /* =========================
   * Public APIs
   * ========================= */

  public static Pageable extendDefaultSort(Pageable pageable) {
    return extendSort(pageable, Order.asc("id"));
  }

  public static Pageable extendDefaultSort(Pageable pageable, Class<?> entityClass) {
    List<Order> orders = new ArrayList<>();

    addIfPresent(orders, entityClass, Id.class, Sort.Direction.ASC);
    addIfPresent(orders, entityClass, LastModifiedDate.class, Sort.Direction.DESC);
    addIfPresent(orders, entityClass, CreatedDate.class, Sort.Direction.DESC);

    return extendSort(pageable, orders);
  }

  /* =========================
   * Core logic
   * ========================= */

  public static Pageable extendSort(Pageable pageable, Order... orders) {
    return extendSort(pageable, Arrays.asList(orders));
  }

  public static Pageable extendSort(Pageable pageable, List<Order> defaultOrders) {
    Sort mergedSort = mergeSort(pageable.getSort(), defaultOrders);
    return pageable.isPaged()
        ? PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), mergedSort)
        : Pageable.unpaged(mergedSort);
  }

  /* =========================
   * Helpers
   * ========================= */

  /** Client sort ALWAYS has higher priority than default sort */
  private static Sort mergeSort(Sort clientSort, List<Order> defaultOrders) {
    if (defaultOrders.isEmpty()) {
      return clientSort;
    }

    Set<String> clientProperties =
        clientSort.stream().map(Order::getProperty).collect(Collectors.toSet());

    List<Order> filteredDefaults =
        defaultOrders.stream().filter(o -> !clientProperties.contains(o.getProperty())).toList();

    return clientSort.and(Sort.by(filteredDefaults));
  }

  private static void addIfPresent(
      List<Order> orders,
      Class<?> entityClass,
      Class<? extends Annotation> annotation,
      Sort.Direction direction) {
    String field = AnnotationUtils.getFirstAnnotationFieldName(entityClass, annotation);
    if (field != null) {
      orders.add(new Order(direction, field));
    }
  }
}
