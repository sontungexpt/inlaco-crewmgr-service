package com.inlaco.crewmgrservice.feature.user.domain.model;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.inlaco.crewmgrservice.feature.user.domain.enums.UserStatus;
import com.inlaco.crewmgrservice.feature.user.domain.enums.UsernameType;
import com.inlaco.crewmgrservice.feature.user.domain.objectvalue.UserStatusHistory;
import com.inlaco.crewmgrservice.shared.constant.PhoneNumberRegexp;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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

  private List<UserStatusHistory> statusHistories = new ArrayList<>();

  private UserAuthority authority;

  public User(String username, String password, UserAuthority right, String name) {
    this.username = username;
    this.password = password;
    this.authority = right;
    this.name = name;
  }

  public UsernameType getUsernameType() {
    if (PhoneNumberRegexp.isValidAny(username)) {
      return UsernameType.PHONE_NUMBER;
    } else {
      return UsernameType.EMAIL;
    }
  }

  public void activate(String reason) {
    changeStatus(UserStatus.ACTIVE, "SYSTEM", reason);
  }

  public void changeStatus(UserStatus newStatus, String changedBy, String reason) {
    if (status == newStatus) return;
    UserStatus old = status;
    status = newStatus;
    statusHistories.add(new UserStatusHistory(old, newStatus, changedBy, reason, Instant.now()));
  }
}
