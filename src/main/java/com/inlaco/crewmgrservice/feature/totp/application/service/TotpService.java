package com.inlaco.crewmgrservice.feature.totp.application.service;

import com.inlaco.crewmgrservice.feature.totp.application.port.in.TotpUseCase;
import com.inlaco.crewmgrservice.feature.totp.application.port.out.TotpRepository;
import com.inlaco.crewmgrservice.feature.totp.domain.exception.TotpDisabledException;
import com.inlaco.crewmgrservice.feature.totp.domain.exception.TotpInvalidCodeException;
import com.inlaco.crewmgrservice.feature.totp.domain.exception.TotpNotFoundException;
import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import com.inlaco.crewmgrservice.feature.totp.infrastructure.config.PurposeCodeVerifierFactory;
import com.inlaco.crewmgrservice.feature.totp.presentation.dto.response.TotpSetupResponse;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TotpService implements TotpUseCase {

  private final TotpRepository totpRepository;
  private final PurposeCodeVerifierFactory verifierFactory;
  private final QrDataFactory qrDataFactory;
  private final QrGenerator qrGenerator;

  @Value("${totp.issuer:Inlaco}")
  private String issuer;

  @Override
  @Transactional
  public TotpSetupResponse setupTotp(
      String userId, String email, TotpSecret.TotpPurpose purpose, String purposeId) {

    // 1. clear old
    totpRepository.deleteByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId);

    // 2. generate secret
    String secret = verifierFactory.getSecretGenerator(purpose).generate();

    // 3. save
    TotpSecret entity = TotpSecret.generateNew(userId, email, secret, purpose, purposeId);

    totpRepository.save(entity);

    // 4. generate QR
    String qrCode = generateQrCode(email, secret, purpose);

    // 5. return response (NO SECRET)
    return TotpSetupResponse.builder().qrCode(qrCode).purpose(purpose).purposeId(purposeId).build();
  }

  private String generateQrCode(String email, String secret, TotpSecret.TotpPurpose purpose) {
    try {
      QrData data =
          qrDataFactory
              .newBuilder()
              .label(email)
              .secret(secret)
              .issuer(issuer + " - " + purpose.name())
              .build();

      return dev.samstevens.totp.util.Utils.getDataUriForImage(
          qrGenerator.generate(data), qrGenerator.getImageMimeType());

    } catch (Exception e) {
      throw new RuntimeException("Failed to generate QR", e);
    }
  }

  @Override
  @Transactional
  public boolean verifyTotp(
      String userId, String code, TotpSecret.TotpPurpose purpose, String purposeId) {

    TotpSecret secret =
        totpRepository
            .findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId)
            .orElseThrow(() -> new TotpNotFoundException("TOTP not found"));

    if (!secret.isEnabled()) {
      throw new TotpDisabledException("TOTP disabled");
    }

    CodeVerifier verifier = verifierFactory.getCodeVerifier(purpose);

    boolean valid = verifier.isValidCode(secret.getSecret(), code);

    if (!valid) {
      throw new TotpInvalidCodeException("Invalid code");
    }

    secret.markAsUsed();
    totpRepository.save(secret);

    return true;
  }

  @Override
  public TotpSecret getTotpSecret(String userId, TotpSecret.TotpPurpose purpose, String purposeId) {

    return totpRepository
        .findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId)
        .orElseThrow(() -> new TotpNotFoundException("Not found"));
  }

  @Override
  @Transactional
  public void disableTotp(String userId, TotpSecret.TotpPurpose purpose, String purposeId) {

    TotpSecret secret = getTotpSecret(userId, purpose, purposeId);
    secret.disable();
    totpRepository.save(secret);
  }

  @Override
  @Transactional
  public void cleanupExpiredTokens(Duration lifetime) {

    var cutoff = java.time.Instant.now().minus(lifetime);

    List<TotpSecret> old = totpRepository.findByCreatedAtBefore(cutoff);

    if (old.isEmpty()) return;

    totpRepository.deleteAllById(old.stream().map(TotpSecret::getId).toList());

    log.info("Cleaned {} expired TOTP", old.size());
  }
}
