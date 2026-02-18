package com.inlaco.crewmgrservice.shared.application.model;

import com.inlaco.crewmgrservice.shared.support.ConsoleUtils;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public sealed interface FieldUpdate<T> permits FieldUpdate.Unchanged, FieldUpdate.Updated {

  final class Unchanged<T> implements FieldUpdate<T> {
    private static final Unchanged<?> INSTANCE = new Unchanged<>();

    private Unchanged() {}

    private static <T> Unchanged<T> instance() {
      return (Unchanged<T>) INSTANCE;
    }
  }

  record Updated<T>(T value) implements FieldUpdate<T> {}

  // =========================
  // Factory methods
  // =========================

  static <T> FieldUpdate<T> unchanged() {
    ConsoleUtils.print("FieldUpdate.unchanged");
    return Unchanged.instance(); // singleton
  }

  static <T> FieldUpdate<T> of(T value) {
    ConsoleUtils.print("FieldUpdate.of", value);
    return new Updated<>(value);
  }

  // =========================
  // State
  // =========================
  default boolean isUpdated() {
    return this instanceof Updated<T>;
  }

  default boolean isUnchanged() {
    return !isUpdated();
  }

  default boolean isExplicitNull() {
    return this instanceof Updated<T> u && u.value() == null;
  }

  // =========================
  // Safe access
  // =========================

  default T get() {
    if (this instanceof Updated<T> u) return u.value();
    throw new IllegalStateException("Field is not updated");
  }

  default T orElse(T defaultValue) {
    if (this instanceof Updated<T> u) {
      return u.value() != null ? u.value() : defaultValue;
    }
    return defaultValue;
  }

  // =========================
  // Functional helpers
  // =========================

  default void ifUpdated(Consumer<T> consumer) {
    Objects.requireNonNull(consumer);
    if (this instanceof Updated<T> u) {
      ConsoleUtils.print("FieldUpdate.ifUpdated", u.value());
      consumer.accept(u.value());
    }
  }

  default <R> FieldUpdate<R> map(Function<T, R> mapper) {
    Objects.requireNonNull(mapper);
    if (this instanceof Updated<T> u) {
      return FieldUpdate.of(mapper.apply(u.value()));
    }
    return FieldUpdate.unchanged();
  }
}
