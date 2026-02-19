package com.inlaco.crewmgrservice.shared.application.model;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;

/**
 * Minimal sealed Patch type representing three states: - Unchanged (singleton) - Updated(value)
 * where value may be null (explicit null)
 *
 * <p>Keep domain behaviour in helpers (PatchUtils) when possible to avoid polluting the interface
 * with many default methods that JSON mappers can introspect.
 */
public sealed interface Patch<T> permits Patch.Unchanged, Patch.Updated {

  final class Unchanged<T> implements Patch<T> {
    private static final Unchanged<?> INSTANCE = new Unchanged<>();

    private Unchanged() {}

    static <T> Unchanged<T> instance() {
      return (Unchanged<T>) INSTANCE;
    }
  }

  /**
   * Updated record holding the new value (may be null to represent explicit null). Keep only a
   * single accessor 'value()' and an optional helper asOptional().
   */
  record Updated<T>(T value) implements Patch<T> {
    /** Return Optional.ofNullable(value) — convenient for internal usage. */
    public Optional<T> asOptional() {
      return Optional.ofNullable(value);
    }
  }

  // Factory methods
  static <T> Patch<T> unchanged() {
    return Unchanged.instance();
  }

  static <T> Patch<T> of(@Nullable T value) {
    return new Updated<>(value);
  }

  // ============ Introspection helpers (minimal) ============
  /** True if this patch is an Updated instance (value may still be null). */
  default boolean isUpdated() {
    return this instanceof Updated;
  }

  default boolean isUnchanged() {
    return !isUpdated();
  }

  /** True only when Updated and value == null (explicitly set to null). */
  default boolean isExplicitNull() {
    return this instanceof Updated<?> u && u.value() == null;
  }

  /**
   * True when patch is Updated and value != null. Useful for validators/extractors that only want
   * to validate non-null updated values.
   */
  default boolean hasValue() {
    return this instanceof Updated<?> u && u.value() != null;
  }

  // ============ Convenience operations (kept simple) ============
  /** Run consumer when updated (value may be null). Return true if updated. */
  default boolean ifUpdated(Consumer<@Nullable T> consumer) {
    Objects.requireNonNull(consumer);
    if (this instanceof Updated<T> u) {
      consumer.accept(u.value());
      return true;
    }
    return false;
  }

  /** Map the contained value when Updated (null preserved). Otherwise return ifUnchanged. */
  default <R> R ifUpdatedOrElse(Function<@Nullable T, R> ifUpdated, Supplier<R> ifUnchanged) {
    if (this instanceof Updated<T> u) {
      return ifUpdated.apply(u.value());
    }
    return ifUnchanged.get();
  }

  /** Map the contained value when Updated (null preserved). Otherwise return unchanged. */
  default <R> Patch<R> map(Function<? super T, ? extends R> mapper) {
    Objects.requireNonNull(mapper);
    if (this instanceof Updated<T> u) {
      T v = u.value();
      return Patch.of(v != null ? mapper.apply(v) : null);
    }
    return Patch.unchanged();
  }

  /**
   * Variant of flatMap that passes the raw (possibly-null) value to the mapper. Mapper must handle
   * nulls explicitly. This avoids exposing Optional at the interface level.
   */
  default <R> Patch<R> flatMapNullable(Function<? super T, Patch<R>> mapper) {
    Objects.requireNonNull(mapper);
    if (this instanceof Updated<T> u) {
      return mapper.apply(u.value());
    }
    return Patch.unchanged();
  }
}
