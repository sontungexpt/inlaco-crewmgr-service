package com.inlaco.crewmgrservice.common.payload;

public interface IPasswordResetRequest extends IBodyRequest {

  String getPassword();

  String getConfirmPassword();
}
