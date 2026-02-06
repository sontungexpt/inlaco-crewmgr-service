package com.inlaco.crewmgrservice.utils;

import com.inlaco.crewmgrservice.shared.constant.PhoneNumberRegexp;
import java.util.Arrays;

public class PhoneNumberValidatorUtils {

  /**
   * CharSequence Validates a phone number according to the ITU-T E.164 standard.
   *
   * @param phoneNumber The phone number to validate
   * @return true if the phone number is valid, false otherwise
   */
  public static final boolean validate(final CharSequence phoneNumber) {
    return isPotentialPhoneNumber(phoneNumber)
        && Arrays.stream(PhoneNumberRegexp.values())
            .anyMatch(regexp -> regexp.isValid(phoneNumber));
  }

  /**
   * Checks if the given phone number is a potential phone number.
   *
   * @param phone The phone number to check
   * @return true if the phone number is a potential phone number, false otherwise
   */
  public static boolean isPotentialPhoneNumber(CharSequence phone) {
    if (phone == null) {
      return false;
    }

    int length = phone.length();

    // check the length of the phone number
    // including the '+' sign
    if (length > 16 || length < 1) {
      return false;
    }

    int start = 0;
    if (phone.charAt(0) == '+') {
      if (length < 2) {
        return false;
      }
      start = 1;
    } else if (length > 15) {
      // because no '+' sign, the length should be less or equal 15
      return false;
    }

    for (int i = start; i < length; i++) {
      if (!Character.isDigit(phone.charAt(i))) {
        return false;
      }
    }

    return true;
  }
}
