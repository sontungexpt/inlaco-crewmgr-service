package com.inlaco.crewmgrservice.feature.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inlaco.crewmgrservice.feature.user.enums.UserStatus;
import com.inlaco.crewmgrservice.feature.user.service.AuthorityResolver;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUser implements UserDetails {

  private final User user;
  private final Collection<GrantedAuthority> authorities;

  public SecurityUser(User user, AuthorityResolver resolver) {
    this.user = user;
    this.authorities = resolver.resolve(user);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public User getUser() {
    return user;
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
