package com.inlaco.crewmgrservice.feature.user.domain.model;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.inlaco.crewmgrservice.common.model.Asset;
import com.inlaco.crewmgrservice.feature.user.domain.enums.UserStatus;
import com.inlaco.crewmgrservice.feature.user.domain.enums.UsernameType;
import com.inlaco.crewmgrservice.feature.user.domain.model.authorization.Right;
import com.inlaco.crewmgrservice.shared.constant.PhoneNumberRegexp;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
  private String id;

  // Why we need this?
  // The public id is a unique identifier for the account.
  // It is used to identify the account in the system.
  // It can be shared to anyone and it is not sensitive information.
  // Why don't use username as the public id?
  // Because the username can be changed by the user,
  // and we need a unique identifier for the account.
  private String pubId = NanoIdUtils.randomNanoId();

  private String username;

  private Asset avatar;

  private String password;

  private String name;

  private UserStatus status = UserStatus.UNVERIFIED;

  private Instant activatedAt;

  public User(String username, String password, Right right, String name) {
    this.username = username;
    this.password = password;
    this.right = right;
    this.name = name;
  }

  public UsernameType getUsernameType() {
    if (PhoneNumberRegexp.isValidAny(username)) {
      return UsernameType.PHONE_NUMBER;
    } else {
      return UsernameType.EMAIL;
    }
  }

  public void activate() {
    status = UserStatus.ACTIVE;
    this.activatedAt = Instant.now();
  }

  private Right right;
}
