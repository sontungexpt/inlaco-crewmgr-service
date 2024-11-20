package com.inlaco.crewmgrservice.feature.contract.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

@Schema(description = "Defines the type of contract available.")
public class ContractType implements Comparable<ContractType>, Serializable {

  @Schema(description = "A contract for the supply of goods or services.")
  public static ContractType SUPPLY_CONTRACT = new ContractType("SUPPLY_CONTRACT");

  @Schema(description = "A contract for employment or labor.")
  public static ContractType LABOR_CONTRACT = new ContractType("LABOR_CONTRACT");

  private ContractType(String name) {
    this.name = name;
  }

  private static final ContractType[] values = new ContractType[] {LABOR_CONTRACT, SUPPLY_CONTRACT};

  private final String name;

  /**
   * Returns an array containing the standard ContractType. Specifically, this method returns an
   * array containing {@link #SUPPLY_CONTRACT}, {@link #LABOR_CONTRACT},
   *
   * <p>Note that the returned value does not include any ContractType methods defined in WebDav.
   */
  public static ContractType[] values() {
    ContractType[] copy = new ContractType[values.length];
    System.arraycopy(values, 0, copy, 0, values.length);
    return copy;
  }

  /**
   * Return an {@code ContractType} object for the given value.
   *
   * @param name the method value as a String
   * @return the corresponding {@code ContractType}
   */
  public static ContractType valueOf(String name) {
    Assert.notNull(name, "Type must not be null");
    return switch (name) {
      case "SUPPLY_CONTRACT" -> SUPPLY_CONTRACT;
      case "LABOR_CONTRACT" -> LABOR_CONTRACT;
      default -> new ContractType(name);
    };
  }

  /** Return the name of this type, e.g. "SUPPLY_CONTRACT", "LABOR_CONTRACT". */
  public String name() {
    return this.name;
  }

  /**
   * Determine whether this {@code ContractType} matches the given type value.
   *
   * @param name the Type name as a String
   * @return {@code true} if it matches, {@code false} otherwise
   * @since 4.2.4
   */
  public boolean matches(String name) {
    return name().equals(name);
  }

  @Override
  public int hashCode() {
    return this.name.hashCode();
  }

  @Override
  public boolean equals(@Nullable Object other) {
    return (this == other || (other instanceof ContractType that && this.name.equals(that.name)));
  }

  @Override
  public String toString() {
    return this.name;
  }

  @Override
  public int compareTo(ContractType o) {
    return this.name.compareTo(o.name);
  }
}
