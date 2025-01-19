package com.inlaco.crewmgrservice.feature.upload.dto;

import lombok.Data;

@Data
public class UploadType {

  private String name;

  private UploadType nestedType;

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    else if (obj == this) return true;
    else if (obj instanceof UploadType that) {
      return this.name.equals(that.name);
    } else if (obj instanceof String that) {
      return this.name.equals(that);
    }
    return false;
  }

  public boolean isNested() {
    return nestedType != null;
  }
}
