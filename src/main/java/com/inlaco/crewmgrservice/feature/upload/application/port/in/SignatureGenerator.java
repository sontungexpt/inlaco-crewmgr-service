package com.inlaco.crewmgrservice.feature.upload.application.port.in;

import java.util.Map;

public interface SignatureGenerator {

  String generate(Map<String, Object> params);
}
