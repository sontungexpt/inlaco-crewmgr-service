package com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.feature.user.domain.enums.UserStatus;
import com.inlaco.crewmgrservice.feature.user.domain.model.UserAuthority;
import com.inlaco.crewmgrservice.feature.user.domain.objectvalue.UserStatusHistory;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "users")
public class UserEntity {
  @Id private String id;

  // Why we need this?
  // The public id is a unique identifier for the account.
  // It is used to identify the account in the system.
  // It can be shared to anyone and it is not sensitive information.
  // Why don't use username as the public id?
  // Because the username can be changed by the user,
  // and we need a unique identifier for the account.
  @Indexed(unique = true)
  private String pubId;

  @Indexed(unique = true)
  private String username;

  private Asset avatar;

  private String password;

  private String name;

  private UserStatus status;

  private List<UserStatusHistory> statusHistories = new ArrayList<>();

  private UserAuthority authority;

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;
}
