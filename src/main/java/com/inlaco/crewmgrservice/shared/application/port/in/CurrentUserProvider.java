package com.inlaco.crewmgrservice.shared.application.port.in;

import com.inlaco.crewmgrservice.shared.objectvalue.CurrentUser;

public interface CurrentUserProvider {
  CurrentUser getUser();
}
