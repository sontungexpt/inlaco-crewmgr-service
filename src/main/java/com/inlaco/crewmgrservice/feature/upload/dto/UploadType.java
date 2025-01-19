package com.inlaco.crewmgrservice.feature.upload.dto;

import com.inlaco.crewmgrservice.feature.upload.enums.IUploadStragegy;
import lombok.Data;

@Data
public class UploadType {

  private IUploadStragegy stragegy;

  private UploadType nestedType;

  @Override
  public int hashCode() {
    return stragegy.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    else if (obj == this) return true;
    else if (obj instanceof UploadType that) {
      return this.stragegy.equals(that.stragegy);
    } else if (obj instanceof String that) {
      return this.stragegy.equals(that);
    }
    return false;
  }

  public boolean isNested() {
    return nestedType != null;
  }
}
