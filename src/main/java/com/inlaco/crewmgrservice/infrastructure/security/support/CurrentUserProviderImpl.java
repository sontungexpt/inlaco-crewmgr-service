package com.inlaco.crewmgrservice.infrastructure.security.support;

import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.application.port.in.CurrentUserProvider;
import com.inlaco.crewmgrservice.shared.objectvalue.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CurrentUserProviderImpl implements CurrentUserProvider {

  @Override
  public CurrentUser getUser() {
    User user = PrincipalUtils.getUser();
    return CurrentUser.builder().id(user.getId()).username(user.getUsername()).build();
  }
}
