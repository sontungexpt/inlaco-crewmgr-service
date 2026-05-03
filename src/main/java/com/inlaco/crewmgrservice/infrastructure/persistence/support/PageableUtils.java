package com.inlaco.crewmgrservice.infrastructure.persistence.support;

import java.lang.annotation.Annotation;
import java.util.*;
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
    return appendSort(pageable, Order.desc("id"));
  }

  public static Pageable enforceIdSort(Pageable pageable, Class<?> entityClass) {
    return buildOrder(entityClass, Id.class)
        .map(order -> appendSort(pageable, order.with(Direction.DESC)))
        .orElse(pageable);
  }

  public static Pageable enforceAuditSort(Pageable pageable, Class<?> entityClass) {
    List<Order> orders = new ArrayList<>(2);
    buildOrder(entityClass, LastModifiedDate.class)
        .ifPresent(o -> orders.add(o.with(Direction.DESC)));
    buildOrder(entityClass, CreatedDate.class).ifPresent(o -> orders.add(o.with(Direction.DESC)));
    return appendSort(pageable, orders);
  }

  public static Pageable appendSort(Pageable pageable, Order... orders) {
    return appendSort(pageable, Arrays.asList(orders));
  }

  public static Pageable appendSort(Pageable pageable, Collection<Sort.Order> orders) {
    return mergeSort(pageable, orders, false);
  }

  public static Pageable prependSort(Pageable pageable, Collection<Sort.Order> orders) {
    return mergeSort(pageable, orders, true);
  }

  public static Pageable prependSort(Pageable pageable, Sort.Order... orders) {
    return prependSort(pageable, Arrays.asList(orders));
  }

  private static Pageable mergeSort(
      Pageable pageable, Collection<Sort.Order> orders, boolean prepend) {
    if (orders == null || orders.isEmpty()) return pageable;

    Sort original = pageable.getSort();
    LinkedHashMap<String, Sort.Order> map = new LinkedHashMap<>();

    if (prepend) {
      for (Sort.Order order : orders) {
        if (order == null) continue;
        map.put(order.getProperty(), order);
      }
      for (Sort.Order order : original) {
        if (order == null) continue;
        map.putIfAbsent(order.getProperty(), order);
      }
    } else {
      for (Sort.Order order : original) {
        if (order == null) continue;
        map.put(order.getProperty(), order);
      }
      for (Sort.Order order : orders) {
        if (order == null) continue;
        map.putIfAbsent(order.getProperty(), order);
      }
    }

    List<Sort.Order> mergedOrders = new ArrayList<>(map.values());

    if (mergedOrders.equals(original.toList())) {
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
