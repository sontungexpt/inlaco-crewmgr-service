package com.inlaco.crewmgrservice.shared.application.port.in;

import com.inlaco.crewmgrservice.shared.objectvalue.Asset;

public interface AssetUrlResolver {
  String resolve(Asset asset);
}
