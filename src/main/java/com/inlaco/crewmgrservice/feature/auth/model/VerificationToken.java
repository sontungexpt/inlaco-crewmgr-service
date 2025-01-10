package com.inlaco.crewmgrservice.feature.auth.model;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

@Getter
@AllArgsConstructor
@SuperBuilder
public abstract class VerificationToken {

  @Id private String id;

  protected String userId;

  protected Instant issuedDate;

  public abstract Instant getExpiryDate();

  public VerificationToken(String userId) {
    this.userId = userId;
    this.issuedDate = Instant.now();
  }

  public abstract boolean isValid(String value);

  public abstract VerificationToken refresh(boolean newToken);

  public VerificationToken refresh() {
    return refresh(false);
  }
  ;

  /**
   * Convert the object to a map representation This is useful for storing the object in a Redis
   * database
   *
   * @return a map representation of the object
   */
  public Map<String, String> toMap() {
    HashMap<String, String> map = new HashMap<>();
    map.put("userId", userId);
    map.put("issuedDate", issuedDate.toString());
    return map;
  }
}
