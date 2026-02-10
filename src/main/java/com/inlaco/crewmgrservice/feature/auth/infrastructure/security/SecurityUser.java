package com.inlaco.crewmgrservice.feature.auth.infrastructure.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inlaco.crewmgrservice.feature.user.enums.UserStatus;
import com.inlaco.crewmgrservice.feature.user.model.User;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record SecurityUser(User user, Collection<? extends GrantedAuthority> authorities)
    implements UserDetails {

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isEnabled() {
    return user.getStatus() == UserStatus.ACTIVE;
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonExpired() {
    return user.getStatus() != UserStatus.ARCHIVED && user.getStatus() != UserStatus.DELETED;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonLocked() {
    return user.getStatus() != UserStatus.BANNED;
  }

  @Override
  @JsonIgnore
  public boolean isCredentialsNonExpired() {
    return user.getStatus() != UserStatus.COMPROMISED;
  }
}
