package com.inlaco.crewmgrservice.shared.application.port.out;

import com.inlaco.crewmgrservice.shared.objectvalue.CurrentUser;

public interface CurrentUserProvider {
  CurrentUser getUser();
}
