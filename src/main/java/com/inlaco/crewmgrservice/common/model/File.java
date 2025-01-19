package com.inlaco.crewmgrservice.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inlaco.crewmgrservice.common.payload.UploadableFile;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class File implements UploadableFile, Serializable {

  private String url;

  private String name;

  @Transient
  @Schema(description = "If new file is uploaded, this token should be used to upload the file")
  private String uploadToken;

  @JsonIgnore
  @Schema(hidden = true)
  public String getUploadToken() {
    return uploadToken;
  }

  public File(String url) {
    this.url = url;
  }

  public File(String url, String name) {
    this.url = url;
    this.name = name;
  }
}
