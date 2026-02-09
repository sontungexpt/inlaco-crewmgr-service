package com.inlaco.crewmgrservice.feature.upload.application.port.in;

import com.inlaco.crewmgrservice.feature.upload.domain.model.CloudinarySignParams;
import java.util.Map;

public interface CloudinaryService {

  Map<String, Object> getUploadOptions(Map<String, Object> paramsToSign);

  Map<String, Object> getUploadOptions(CloudinarySignParams signParams);
}
