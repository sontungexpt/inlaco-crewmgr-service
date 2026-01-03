package com.inlaco.crewmgrservice.feature.upload.enums;

import com.inlaco.crewmgrservice.common.model.ICustomEnum;

public class UploadStrategy implements ICustomEnum {

  public static UploadStrategy DEFAULT = new UploadStrategy("DEFAULT");
  public static UploadStrategy COURSE_WALLPAPER = new UploadStrategy("COURSE_WALLPAPER");
  public static UploadStrategy TRAINING_PROVIDER_LOGO =
      new UploadStrategy("TRAINING_PROVIDER_LOGO");
  public static UploadStrategy RESUME = new UploadStrategy("RESUME");
  public static UploadStrategy CONTRACT_FILE = new UploadStrategy("CONTRACT_FILE");
  public static UploadStrategy CONTRACT_TEMPLATE = new UploadStrategy("CONTRACT_TEMPLATE");
  public static UploadStrategy CREW_RENTAL_REQUEST_DETAIL_FILE =
      new UploadStrategy("CREW_RENTAL_REQUEST_DETAIL_FILE");
  public static UploadStrategy SHIP_IMAGE = new UploadStrategy("SHIP_IMAGE");

  private final String value;

  public UploadStrategy(String value) {
    this.value = value;
  }

  @Override
  public String name() {
    return value;
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof UploadStrategy) {
      return value.equals(((UploadStrategy) obj).value);
    }
    return false;
  }
}
