package com.inlaco.crewmgrservice.feature.auth.service;

import com.inlaco.crewmgrservice.feature.auth.model.RefreshToken;

public interface TokenService {

  String generateAccessToken(String userPubId);

  RefreshToken generateRefreshToken(String userPubId);
}
