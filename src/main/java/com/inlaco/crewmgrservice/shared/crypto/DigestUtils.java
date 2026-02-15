package com.inlaco.crewmgrservice.shared.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class DigestUtils {

  private DigestUtils() {}

  public static String SHA_256 = "SHA-256";

  public static String sha256(String input) {
    return digest(SHA_256, input);
  }

  public static String digest(String alg, String input) {
    try {
      MessageDigest md = MessageDigest.getInstance(alg);
      byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
      return toHex(digest);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalArgumentException("Unsupported algorithm: " + alg, e);
    }
  }

  private static String toHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder(bytes.length * 2);
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }
}
