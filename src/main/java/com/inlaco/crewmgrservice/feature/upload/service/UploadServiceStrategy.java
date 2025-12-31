package com.inlaco.crewmgrservice.feature.upload.service;

import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.upload.enums.UploadStrategy;
import java.util.Map;

public interface UploadServiceStrategy {

  public UploadStrategy getStragegy();

  public Map<String, Object> getUploadOptions(Object payload);

  public File metadata(String publicId);
}
