package com.inlaco.crewmgrservice.feature.user.model;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.inlaco.crewmgrservice.feature.user.enums.UserStatus;
import com.inlaco.crewmgrservice.feature.user.enums.UsernameType;
import com.inlaco.crewmgrservice.feature.user.model.authorization.Right;
import com.inlaco.crewmgrservice.feature.user.model.state.job.CircleJobStateContext;
import com.inlaco.crewmgrservice.infrastructure.web.validation.annotation.OptimizedName;
import com.inlaco.crewmgrservice.infrastructure.web.validation.password.Password;
import com.inlaco.crewmgrservice.infrastructure.web.validation.username.Username;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "User model", name = "User")
@Document(collection = "users")
public class User implements /* UserDetails, */ Persistable<String> {
  @JsonIgnore @Id private String id;

  // Why we need this?
  // The public id is a unique identifier for the account.
  // It is used to identify the account in the system.
  // It can be shared to anyone and it is not sensitive information.
  // Why don't use username as the public id?
  // Because the username can be changed by the user,
  // and we need a unique identifier for the account.
  @Default
  @Indexed(unique = true)
  @Schema(description = "The public id of the account")
  private String pubId = NanoIdUtils.randomNanoId();

  @Indexed(unique = true)
  @Schema(
      description = "The phone number of the account or email",
      examples = {
        "tunggitclone03@gmail.com",
        "0392211343",
      },
      requiredMode = RequiredMode.REQUIRED)
  @Username
  private String username;

  @Schema(description = "The avatar of the account")
  private String avatar;

  @NotNull
  @Schema(description = "The type of the username", requiredMode = RequiredMode.REQUIRED)
  private UsernameType usernameType;

  @JsonIgnore
  @Password
  @Schema(
      description = "The password of the account",
      example = "Admin123",
      requiredMode = RequiredMode.REQUIRED)
  private String password;

  @OptimizedName
  @Schema(description = "The name of the account", requiredMode = RequiredMode.REQUIRED)
  private String name;

  @Schema(description = "The last logout time of the account")
  @JsonIgnore
  private Instant lastLogoutAt;

  @Default
  @Schema(description = "The status of the account")
  private UserStatus status = UserStatus.UNVERIFIED;

  @Schema(description = "The activated time of the account")
  private Instant activatedAt;

  public void activate() {
    status = UserStatus.ACTIVE;
    this.activatedAt = Instant.now();
  }

  @Schema(description = "The roles of the account")
  private Right right;

  @JsonIgnore
  @CreatedDate
  @Schema(description = "The created time of the account")
  private Instant createdAt;

  @Schema(description = "The updated time of the account")
  @JsonIgnore
  @LastModifiedDate
  private Instant updatedAt;

  public enum JobState {
    CAN_APPLY,
    CANDIDATE,
    SAILOR
  }

  @Default
  @Schema(description = "The job state of the account")
  private JobState jobState = JobState.CAN_APPLY;

  public void promoteJobState() {
    new CircleJobStateContext(this).promote();
  }

  public void demoteJobState() {
    new CircleJobStateContext(this).demote();
  }

  // public boolean hasRole(Role.Name roleName) {
  //   return right.getRoles().stream().anyMatch(r -> r.getName().equals(roleName));
  // }

  public User(User user) {
    this.id = user.getId();
    this.pubId = user.getPubId();
    this.username = user.getUsername();
    this.usernameType = user.getUsernameType();
    this.password = user.getPassword();
    this.name = user.getName();
    this.jobState = user.getJobState();
    this.lastLogoutAt = user.getLastLogoutAt();
    this.status = user.getStatus();
    this.right = user.getRight();
    this.createdAt = user.getCreatedAt();
    this.updatedAt = user.getUpdatedAt();
  }

  @JsonIgnore
  public String getPassword() {
    return password;
  }

  public String getUsername() {
    return username;
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
      return id.equals(that.id) || username.equals(that.username) || pubId.equals(that.pubId);
    }
    return false;
  }

  @Override
  @JsonIgnore
  public boolean isNew() {
    return createdAt == null || id == null;
  }
}
