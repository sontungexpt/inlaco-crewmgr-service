package com.inlaco.crewmgrservice.feature.user.model;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.inlaco.crewmgrservice.feature.user.enums.RoleType;
import com.inlaco.crewmgrservice.feature.user.enums.UserStatus;
import com.inlaco.crewmgrservice.feature.user.model.authorization.Role;
import com.inlaco.crewmgrservice.validation.annotation.OptimizedName;
import com.inlaco.crewmgrservice.validation.annotation.Password;
import com.inlaco.crewmgrservice.validation.annotation.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@SuperBuilder
@Schema(description = "User model", name = "User")
@Document(collection = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class User implements UserDetails, Persistable<String> {
  @JsonIgnore @Id private String id;

  // Why we need this?
  // The public id is a unique identifier for the account.
  // It is used to identify the account in the system.
  // It can be shared to anyone and it is not sensitive information.
  // Why don't use phoneNumber as the public id?
  // Because the phoneNumber can be changed by the user,
  // and we need a unique identifier for the account.
  @Default
  @Indexed(unique = true)
  @Schema(description = "The public id of the account")
  private String pubId = NanoIdUtils.randomNanoId();

  @PhoneNumber
  @Indexed(unique = true)
  @Schema(description = "The phone number of the account", requiredMode = RequiredMode.REQUIRED)
  private String phoneNumber;

  @JsonIgnore
  @Password
  @Schema(description = "The password of the account", requiredMode = RequiredMode.REQUIRED)
  private String password;

  @OptimizedName
  @Schema(description = "The name of the account", requiredMode = RequiredMode.REQUIRED)
  private String name;

  @Email
  @Schema(description = "The email of the user", requiredMode = RequiredMode.REQUIRED)
  private String email;

  @Schema(description = "The last logout time of the account")
  @JsonIgnore
  private Instant lastLogoutAt;

  @Default
  @Schema(description = "The status of the account")
  private UserStatus status = UserStatus.UNVERIFIED;

  @Schema(description = "The roles of the account")
  private Set<Role> roles;

  @CreatedDate
  @Schema(description = "The created time of the account")
  @JsonIgnore
  private Instant createdAt;

  @LastModifiedDate
  @Schema(description = "The updated time of the account")
  @JsonIgnore
  private Instant updatedAt;

  public enum JobState {
    CAN_APPLY,
    CANDIDATE,
    EMPLOYEE
  }

  private JobState jobState;

  @JsonIgnore @Transient private Collection<? extends GrantedAuthority> authorities;

  public boolean hasRole(RoleType roleType) {
    return roles.stream().anyMatch(r -> r.getName().equals(roleType));
  }

  public User(User user) {
    this.id = user.getId();
    this.pubId = user.getPubId();
    this.phoneNumber = user.getPhoneNumber();
    this.password = user.getPassword();
    this.name = user.getName();
    this.email = user.getEmail();
    this.lastLogoutAt = user.getLastLogoutAt();
    this.status = user.getStatus();
    this.roles = user.getRoles();
    this.createdAt = user.getCreatedAt();
    this.updatedAt = user.getUpdatedAt();
    this.authorities = user.getAuthorities();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (roles == null) {
      return Collections.emptySet();
    } else {
      if (authorities == null || authorities.isEmpty()) {
        Collection<SimpleGrantedAuthority> auths = new HashSet<>();
        // roles.forEach(
        //     role -> {
        //       auths.add(new SimpleGrantedAuthority("ROLE_" + role.getName().name()));
        //       if (role.getPermissions() != null) {
        //         role.getPermissions().stream()
        //             .forEach(
        //                 permission -> {
        //                   auths.add(new SimpleGrantedAuthority(permission.name()));
        //                 });
        //       }
        //     });
        authorities = auths;
      }
      return authorities;
    }
  }

  @Override
  @JsonIgnore
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return phoneNumber;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonExpired() {
    return status != UserStatus.ARCHIVED && status != UserStatus.DELETED;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonLocked() {
    return status != UserStatus.BANNED;
  }

  @Override
  @JsonIgnore
  public boolean isCredentialsNonExpired() {
    return status != UserStatus.COMPROMISED;
  }

  @JsonIgnore
  @Override
  public boolean isEnabled() {
    return status == UserStatus.ACTIVE;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    else if (obj instanceof User) {
      User that = (User) obj;
      return id.equals(that.getId())
          || phoneNumber.equals(that.getPhoneNumber())
          || pubId.equals(that.getPubId());
    }
    return false;
  }

  @Override
  @JsonIgnore
  public boolean isNew() {
    return createdAt == null || id == null;
  }
}
