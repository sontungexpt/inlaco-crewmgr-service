//package com.inlaco.crewmgrservice.feature.totp.application.service;
//
//import com.inlaco.crewmgrservice.feature.totp.application.port.out.TotpRepository;
//import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
//import com.inlaco.crewmgrservice.feature.totp.domain.exception.TotpException;
//import com.inlaco.crewmgrservice.feature.totp.presentation.dto.response.TotpSetupResponse;
//import dev.samstevens.totp.code.CodeVerifier;
//import dev.samstevens.totp.qr.QrGenerator;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TotpServiceTest {
//
//  @Mock
//  private TotpRepository totpRepository;
//
//  @Mock
//  private QrGenerator qrGenerator;
//
//  @Mock
//  private CodeVerifier codeVerifier;
//
//  @InjectMocks
//  private TotpService totpService;
//
//  private final String userId = "test@example.com";
//  private final String email = "test@example.com";
//  private final TotpSecret.TotpPurpose purpose = TotpSecret.TotpPurpose.API_KEY_CREATION;
//  private final String purposeId = "test-purpose-123";
//
//  @BeforeEach
//  void setUp() {
//    // Reset mocks before each test
//    reset(totpRepository, qrGenerator, codeVerifier);
//  }
//
//  @Test
//  void setupTotp_ShouldCreateTotpSecretAndQrCode() {
//    // Given
//    when(totpRepository.save(any(TotpSecret.class))).thenAnswer(invocation -> invocation.getArgument(0));
//    when(qrGenerator.generate(any())).thenReturn("data:image/png;base64,test-qr-code");
//
//    // When
//    TotpSetupResponse response = totpService.setupTotp(userId, email, purpose, purposeId);
//
//    // Then
//    assertNotNull(response);
//    assertNotNull(response.getSecret());
//    assertNotNull(response.getQrCode());
//    assertEquals(purposeId, response.getPurposeId());
//    assertEquals(purpose, response.getPurpose());
//    assertNotNull(response.getInstructions());
//
//    verify(totpRepository).deleteByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId);
//    verify(totpRepository).save(any(TotpSecret.class));
//    verify(qrGenerator).generate(any());
//  }
//
//  @Test
//  void verifyTotp_WithValidCode_ShouldReturnTrue() {
//    // Given
//    TotpSecret totpSecret = TotpSecret.generateNew(userId, email, purpose, purposeId);
//    when(totpRepository.findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId))
//        .thenReturn(Optional.of(totpSecret));
//    when(codeVerifier.isValidCode(totpSecret.getSecret(), "123456")).thenReturn(true);
//    when(totpRepository.save(any(TotpSecret.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//    // When
//    boolean result = totpService.verifyTotp(userId, "123456", purpose, purposeId);
//
//    // Then
//    assertTrue(result);
//    verify(totpRepository).save(totpSecret);
//    assertNotNull(totpSecret.getLastUsedAt());
//    assertEquals(1, totpSecret.getVerificationCount());
//  }
//
//  @Test
//  void verifyTotp_WithInvalidCode_ShouldReturnFalse() {
//    // Given
//    TotpSecret totpSecret = TotpSecret.generateNew(userId, email, purpose, purposeId);
//    when(totpRepository.findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId))
//        .thenReturn(Optional.of(totpSecret));
//    when(codeVerifier.isValidCode(totpSecret.getSecret(), "wrong")).thenReturn(false);
//
//    // When
//    boolean result = totpService.verifyTotp(userId, "wrong", purpose, purposeId);
//
//    // Then
//    assertFalse(result);
//    verify(totpRepository, never()).save(any());
//  }
//
//  @Test
//  void verifyTotp_WithNonExistentSecret_ShouldThrowException() {
//    // Given
//    when(totpRepository.findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId))
//        .thenReturn(Optional.empty());
//
//    // When & Then
//    assertThrows(TotpException.class, () -> totpService.verifyTotp(userId, "123456", purpose, purposeId));
//    verify(codeVerifier, never()).isValidCode(anyString(), anyString());
//  }
//
//  @Test
//  void verifyTotp_WithDisabledSecret_ShouldThrowException() {
//    // Given
//    TotpSecret disabledSecret = TotpSecret.generateNew(userId, email, purpose, purposeId);
//    disabledSecret.disable();
//    when(totpRepository.findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId))
//        .thenReturn(Optional.of(disabledSecret));
//
//    // When & Then
//    assertThrows(TotpException.class, () -> totpService.verifyTotp(userId, "123456", purpose, purposeId));
//    verify(codeVerifier, never()).isValidCode(anyString(), anyString());
//  }
//
//  @Test
//  void getTotpSecret_ShouldReturnSecret() {
//    // Given
//    TotpSecret totpSecret = TotpSecret.generateNew(userId, email, purpose, purposeId);
//    when(totpRepository.findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId))
//        .thenReturn(Optional.of(totpSecret));
//
//    // When
//    TotpSecret result = totpService.getTotpSecret(userId, purpose, purposeId);
//
//    // Then
//    assertEquals(totpSecret, result);
//  }
//
//  @Test
//  void getTotpSecret_WithNonExistentSecret_ShouldReturnNull() {
//    // Given
//    when(totpRepository.findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId))
//        .thenReturn(Optional.empty());
//
//    // When
//    TotpSecret result = totpService.getTotpSecret(userId, purpose, purposeId);
//
//    // Then
//    assertNull(result);
//  }
//
//  @Test
//  void disableTotp_ShouldDisableSecret() {
//    // Given
//    TotpSecret totpSecret = TotpSecret.generateNew(userId, email, purpose, purposeId);
//    when(totpRepository.findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId))
//        .thenReturn(Optional.of(totpSecret));
//    when(totpRepository.save(any(TotpSecret.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//    // When
//    totpService.disableTotp(userId, purpose, purposeId);
//
//    // Then
//    assertTrue(totpSecret.isEnabled() == false);
//    verify(totpRepository).save(totpSecret);
//  }
//
//  @Test
//  void disableTotp_WithNonExistentSecret_ShouldThrowException() {
//    // Given
//    when(totpRepository.findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId))
//        .thenReturn(Optional.empty());
//
//    // When & Then
//    assertThrows(TotpException.class, () -> totpService.disableTotp(userId, purpose, purposeId));
//  }
//}
