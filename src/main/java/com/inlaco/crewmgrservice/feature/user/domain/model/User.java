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

/**
 * Represents a user account in the crew management system.
 *
 * <p>Users are the primary actors in the system, representing individuals who can authenticate and
 * perform various operations based on their authority level. Each user has a unique public
 * identifier that remains stable even if the username changes.
 *
 * <p>The user status determines their ability to access the system and perform operations. Status
 * changes are tracked with audit trails for compliance and security purposes.
 *
 * @author Trần Võ Sơn Tùng
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class User {
  private String id;

  /**
   * Public identifier for the user account.
   *
   * <p>This is a unique identifier that can be safely shared and is not sensitive. Unlike username,
   * this ID remains stable even when users change their username, providing a reliable way to
   * reference user accounts across the system.
   */
  private String pubId = NanoIdUtils.randomNanoId();

  private String username;

  private Asset avatar;

  private String password;

  private String name;

  private UserStatus status = UserStatus.UNVERIFIED;

  private List<UserStatusHistory> statusHistories = new ArrayList<>();

  private UserAuthority authority;

  /**
   * Constructs a new User with basic credentials and authority.
   *
   * @param username the user's username (email or phone number)
   * @param password the user's encrypted password
   * @param right the user's authority level
   * @param name the user's display name
   */
  public User(String username, String password, UserAuthority right, String name) {
    this.username = username;
    this.password = password;
    this.authority = right;
    this.name = name;
  }

  /**
   * Determines the type of username based on its format.
   *
   * <p>This method analyzes the username to determine if it's a phone number or email address,
   * which affects validation and communication methods.
   *
   * @return PHONE_NUMBER if username matches phone pattern, EMAIL otherwise
   */
  public UsernameType getUsernameType() {
    if (PhoneNumberRegexp.isValidAny(username)) {
      return UsernameType.PHONE_NUMBER;
    } else {
      return UsernameType.EMAIL;
    }
  }

  /**
   * Activates the user account with a system-generated reason.
   *
   * <p>This is a convenience method that changes the user status to ACTIVE using "SYSTEM" as the
   * changedBy parameter. Typically used for automated activation processes.
   *
   * @param reason the reason for activation
   */
  public void activate(String reason) {
    changeStatus(UserStatus.ACTIVE, "SYSTEM", reason);
  }

  /**
   * Changes the user's status with audit trail.
   *
   * <p>This method updates the user's status and records the change in the status history for audit
   * purposes. The change includes who made the change, when it was made, and the reason for the
   * change.
   *
   * @param newStatus the new status to set
   * @param changedBy who made the status change
   * @param reason the reason for the status change
   */
  public void changeStatus(UserStatus newStatus, String changedBy, String reason) {
    if (status == newStatus) return;
    UserStatus old = status;
    status = newStatus;
    statusHistories.add(new UserStatusHistory(old, newStatus, changedBy, reason, Instant.now()));
  }
}
