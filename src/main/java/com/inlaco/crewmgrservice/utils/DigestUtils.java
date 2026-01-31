package com.inlaco.crewmgrservice.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class DigestUtils {

  // Source - https://stackoverflow.com/a/36163051
  // Posted by Elliott Frisch
  // Retrieved 2026-01-31, License - CC BY-SA 3.0
  private static String encodeHex(byte[] digest) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < digest.length; i++) {
      sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
    }
    return sb.toString();
  }

  // Source - https://stackoverflow.com/a/36163051
  // Posted by Elliott Frisch
  // Retrieved 2026-01-31, License - CC BY-SA 3.0
  public static String digest(String alg, String input) {
    try {
      MessageDigest md = MessageDigest.getInstance(alg);
      byte[] buffer = input.getBytes("UTF-8");
      md.update(buffer);
      byte[] digest = md.digest();
      return encodeHex(digest);
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }
}
