package com.inlaco.crewmgrservice.feature.shipschedule.application.service;

import com.inlaco.crewmgrservice.feature.shipschedule.application.config.QrTokenProperties;
import com.inlaco.crewmgrservice.feature.shipschedule.application.model.AttendanceQrClaims;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.AttendanceMethod;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.CheckType;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.error.AttendanceErrorCode;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.exception.AttendanceQRCodeException;
import com.inlaco.crewmgrservice.shared.jwt.JwtKeyProvider;
import com.inlaco.crewmgrservice.shared.jwt.JwtTokenGenerator;
import com.inlaco.crewmgrservice.shared.jwt.JwtTokenParser;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class AttendanceQrTokenService {

  private final JwtTokenGenerator generator;
  private final JwtTokenParser parser;
  private final QrTokenProperties qrTokenProperties;
  private final SecretKey checkinSecretKey;
  private final SecretKey checkoutSecretKey;
  private final String TOKEN_SEPARATOR = "_";

  public AttendanceQrTokenService(
      JwtTokenGenerator generator, JwtTokenParser parser, QrTokenProperties qrTokenProperties) {

    this.generator = generator;
    this.parser = parser;
    this.qrTokenProperties = qrTokenProperties;

    this.checkinSecretKey =
        JwtKeyProvider.getSigningKey(qrTokenProperties.getCheckin().getSecretKey());
    this.checkoutSecretKey =
        JwtKeyProvider.getSigningKey(qrTokenProperties.getCheckout().getSecretKey());
  }

  public String generate(AttendanceQrClaims claims) {

    String checkTypeStr = claims.getCheckType().name();
    Map<String, Object> payload = new HashMap<>();

    payload.put("shipScheduleId", claims.getShipScheduleId());
    payload.put("checkType", checkTypeStr);
    payload.put("method", AttendanceMethod.QR_CODE.name());
    payload.put("generatedBy", claims.getGeneratedBy());
    payload.put("generatedAt", claims.getGeneratedAt().toEpochMilli());

    return checkTypeStr
        + TOKEN_SEPARATOR
        + generator.generate(
            claims.getShipScheduleId(),
            payload,
            getExpiration(claims.getCheckType()),
            getSigningKey(claims.getCheckType()));
  }

  public AttendanceQrClaims parse(String token) {
    String[] parts = token.split(TOKEN_SEPARATOR, 2);
    if (parts.length != 2) {
      throw new AttendanceQRCodeException(
          AttendanceErrorCode.ATTENDANCE_QR_INVALID, "Invalid QR token format");
    }

    CheckType checkType = CheckType.valueOf(parts[0]);
    String jwtToken = parts[1];
    SecretKey signingKey = getSigningKey(checkType);

    Map<String, Object> claims = parser.parse(jwtToken, signingKey);
    CheckType claimsCheckType = CheckType.valueOf((String) claims.get("checkType"));

    if (!checkType.equals(claimsCheckType)) {
      throw new AttendanceQRCodeException(
          AttendanceErrorCode.ATTENDANCE_QR_INVALID, "Invalid QR token format");
    }

    return AttendanceQrClaims.builder()
        .shipScheduleId((String) claims.get("shipScheduleId"))
        .checkType(checkType)
        .method(AttendanceMethod.valueOf((String) claims.get("method")))
        .generatedBy((String) claims.get("generatedBy"))
        .generatedAt(Instant.ofEpochMilli((Long) claims.get("generatedAt")))
        .build();
  }

  private SecretKey getSigningKey(CheckType checkType) {
    return switch (checkType) {
      case IN -> checkinSecretKey;
      case OUT -> checkoutSecretKey;
    };
  }

  private long getExpiration(CheckType checkType) {
    return switch (checkType) {
      case IN -> qrTokenProperties.getCheckin().getExpiration();
      case OUT -> qrTokenProperties.getCheckout().getExpiration();
    };
  }
}
