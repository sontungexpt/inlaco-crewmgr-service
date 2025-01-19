package com.inlaco.crewmgrservice.feature.upload.dto;

import com.inlaco.crewmgrservice.common.model.File;
import java.util.List;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class UploadToken {

  private String token;

  private List<File> files;

  public File getFirstFile() {
    return files.get(0);
  }
}
