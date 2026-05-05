package com.inlaco.crewmgrservice.feature.totp.application.service;

import com.inlaco.crewmgrservice.feature.totp.application.port.in.TotpUseCase;
import com.inlaco.crewmgrservice.feature.totp.application.port.out.TotpRepository;
import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import com.inlaco.crewmgrservice.feature.totp.domain.exception.TotpException;
import com.inlaco.crewmgrservice.feature.totp.presentation.dto.response.TotpSetupResponse;
import com.inlaco.crewmgrservice.feature.totp.infrastructure.config.PurposeCodeVerifierFactory;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TotpService implements TotpUseCase {

  private final TotpRepository totpRepository;
  private final QrGenerator qrGenerator;
  private final PurposeCodeVerifierFactory purposeCodeVerifierFactory;
  private final TotpConfigProperties totpConfig;

  @Value("${inlaco.server.base-url}")
  private String baseUrl;

  @Override
  @Transactional
  public TotpSetupResponse setupTotp(String userId, String email, TotpSecret.TotpPurpose purpose, String purposeId) {
    // Delete any existing TOTP for this purpose
    totpRepository.deleteByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId);
    
    // Generate new TOTP secret
    TotpSecret totpSecret = TotpSecret.generateNew(userId, email, purpose, purposeId);
    totpRepository.save(totpSecret);
    
    // Generate QR code
    String qrCode = generateQrCode(totpSecret);
    
    return TotpSetupResponse.builder()
        .secret(totpSecret.getSecret())
        .qrCode(qrCode)
        .purposeId(purposeId)
        .purpose(purpose)
        .instructions(getInstructionsForPurpose(purpose))
        .build();
  }

  @Override
  @Transactional
  public boolean verifyTotp(String userId, String code, TotpSecret.TotpPurpose purpose, String purposeId) {
    TotpSecret totpSecret = totpRepository.findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId)
        .orElseThrow(() -> new TotpException("TOTP not found for this request"));
    
    if (!totpSecret.isEnabled()) {
      throw new TotpException("TOTP is disabled");
    }
    
    // Verify the code using purpose-specific verifier from Spring Boot starter
    CodeVerifier verifier = purposeCodeVerifierFactory.getVerifierForPurpose(purpose);
    boolean isValid = verifier.isValidCode(totpSecret.getSecret(), code);
    
    if (isValid) {
      totpSecret.markAsUsed();
      totpRepository.save(totpSecret);
      log.info("TOTP verified successfully for userId={}, purpose={}", userId, purpose);
    } else {
      log.warn("Invalid TOTP attempt for userId={}, purpose={}", userId, purpose);
    }
    
    return isValid;
  }

  @Override
  public TotpSecret getTotpSecret(String userId, TotpSecret.TotpPurpose purpose, String purposeId) {
    return totpRepository.findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId)
        .orElse(null);
  }

  @Override
  @Transactional
  public void disableTotp(String userId, TotpSecret.TotpPurpose purpose, String purposeId) {
    TotpSecret totpSecret = totpRepository.findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId)
        .orElseThrow(() -> new TotpException("TOTP not found for this request"));
    
    totpSecret.disable();
    totpRepository.save(totpSecret);
  }

  @Override
  @Transactional
  public void cleanupExpiredTokens() {
    List<TotpSecret> oldSecrets = totpRepository.findByCreatedAtBefore(
        java.time.Instant.now().minusSeconds(30L * 24 * 60 * 60)); // 30 days ago
    
    oldSecrets.forEach(secret -> totpRepository.deleteById(secret.getId()));
    
    log.info("Cleaned up {} old TOTP secrets", oldSecrets.size());
  }

  private String generateQrCode(TotpSecret totpSecret) {
    try {
      QrData data = new QrData.Builder()
          .label(totpSecret.getEmail())
          .secret(totpSecret.getSecret())
          .issuer(totpConfig.getIssuer() + " - " + totpSecret.getPurpose())
          .build();
      
      return Utils.getDataUriForImage(
          qrGenerator.generate(data),
          qrGenerator.getImageMimeType()
      );
    } catch (QrGenerationException e) {
      log.error("Failed to generate QR code for TOTP", e);
      throw new TotpException("Failed to generate QR code", e);
    }
  }

  private String getInstructionsForPurpose(TotpSecret.TotpPurpose purpose) {
    TotpConfigProperties.PurposeConfig config = totpConfig.getConfigForPurpose(purpose);
    int timeWindow = config.getTimePeriod() * (2 * config.getTimeTolerance() + 1);
    
    return switch (purpose) {
      case API_KEY_CREATION -> 
          "1. Scan the QR code with your authenticator app (Google Authenticator, Authy, etc.)\n" +
          "2. Enter the 6-digit code when prompted during API key creation\n" +
          "3. High security: Code valid for " + timeWindow + " seconds only";
      case SECRET_KEY_VIEWING -> 
          "1. Scan the QR code with your authenticator app\n" +
          "2. Enter the 6-digit code to view API key secrets\n" +
          "3. Medium security: Code valid for " + timeWindow + " seconds";
      case PASSWORD_RESET -> 
          "1. Scan the QR code with your authenticator app\n" +
          "2. Enter the 6-digit code to reset your password\n" +
          "3. User friendly: Code valid for " + timeWindow + " seconds";
      case ACCOUNT_VERIFICATION -> 
          "1. Scan the QR code with your authenticator app\n" +
          "2. Enter the 6-digit code to verify your account\n" +
          "3. Standard security: Code valid for " + timeWindow + " seconds";
    };
  }
}
