package com.inlaco.crewmgrservice.domain.model;

public record Slug(String value) implements CharSequence {

  @Override
  public int length() {
    return value.length();
  }

  @Override
  public char charAt(int i) {
    return value.charAt(i);
  }

  @Override
  public CharSequence subSequence(int s, int e) {
    return value.subSequence(s, e);
  }

  @Override
  public String toString() {
    return value;
  }
}
