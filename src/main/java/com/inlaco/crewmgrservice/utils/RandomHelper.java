package com.inlaco.crewmgrservice.utils;

public class RandomHelper {

  public static char randomChar() {
    return Math.random() > 0.5 ? randomLowercaseChar() : randomUppercaseChar();
  }

  public static char randomLowercaseChar() {
    return (char) (Math.random() * 26 + 'a');
  }

  public static char randomUppercaseChar() {
    return (char) (Math.random() * 26 + 'A');
  }

  public static int randomNum() {
    return (int) (Math.random() * 10);
  }

  public static char randomNumOrChar() {
    return Math.random() > 0.5 ? randomChar() : (char) randomNum();
  }

  public static String randomString(int length) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < length; i++) {
      builder.append(randomNumOrChar());
    }
    return builder.toString();
  }
}
