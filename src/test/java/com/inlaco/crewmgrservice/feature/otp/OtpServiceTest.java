//package com.inlaco.crewmgrservice.feature.otp;
//
//import com.inlaco.crewmgrservice.feature.otp.application.port.in.OtpUseCase;
//import com.inlaco.crewmgrservice.feature.otp.domain.model.Otp;
//import com.inlaco.crewmgrservice.feature.otp.domain.exception.OtpExpiredException;
//import com.inlaco.crewmgrservice.feature.otp.domain.exception.OtpInvalidException;
//import com.inlaco.crewmgrservice.feature.otp.domain.exception.OtpNotFoundException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class OtpServiceTest {
//
//    @Mock
//    private com.inlaco.crewmgrservice.feature.otp.application.port.out.OtpRepository otpRepository;
//
//    @Mock
//    private com.inlaco.crewmgrservice.feature.notify.sender.NotificationDispatcher notificationDispatcher;
//
//    @InjectMocks
//    private OtpService otpService;
//
//    @Test
//    void testSendOtpViaEmail() {
//        // Given
//        String userId = "user123";
//        String email = "test@example.com";
//        Otp.OtpPurpose purpose = Otp.OtpPurpose.API_KEY_CREATION;
//        String purposeId = "test-purpose-id";
//        Otp.OtpSenderType senderType = Otp.OtpSenderType.EMAIL;
//
//        // When
//        String result = otpService.sendOtp(userId, email, purpose, purposeId, senderType);
//
//        // Then
//        assertNotNull(result);
//        verify(otpRepository).deleteByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId);
//        verify(otpRepository).save(any(Otp.class));
//        verify(notificationDispatcher).sendNotification(any());
//    }
//
//    @Test
//    void testVerifyOtpSuccess() {
//        // Given
//        String userId = "user123";
//        String otpCode = "123456";
//        Otp.OtpPurpose purpose = Otp.OtpPurpose.API_KEY_CREATION;
//        String purposeId = "test-purpose-id";
//
//        Otp existingOtp = Otp.generateNew(userId, "test@example.com", purpose, purposeId, Otp.OtpSenderType.EMAIL);
//        when(otpRepository.findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId))
//            .thenReturn(java.util.Optional.of(existingOtp));
//
//        // When
//        boolean result = otpService.verifyOtp(userId, otpCode, purpose, purposeId);
//
//        // Then
//        assertTrue(result);
//        verify(otpRepository).save(existingOtp);
//    }
//
//    @Test
//    void testVerifyOtpNotFound() {
//        // Given
//        String userId = "user123";
//        String otpCode = "123456";
//        Otp.OtpPurpose purpose = Otp.OtpPurpose.API_KEY_CREATION;
//        String purposeId = "test-purpose-id";
//
//        when(otpRepository.findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId))
//            .thenReturn(java.util.Optional.empty());
//
//        // When & Then
//        OtpNotFoundException exception = assertThrows(
//            OtpNotFoundException.class,
//            () -> otpService.verifyOtp(userId, otpCode, purpose, purposeId)
//        );
//
//        assertEquals("OTP not found for this request", exception.getMessage());
//    }
//
//    @Test
//    void testVerifyOtpExpired() {
//        // Given
//        String userId = "user123";
//        String otpCode = "123456";
//        Otp.OtpPurpose purpose = Otp.OtpPurpose.API_KEY_CREATION;
//        String purposeId = "test-purpose-id";
//
//        Otp expiredOtp = Otp.generateNew(userId, "test@example.com", purpose, purposeId, Otp.OtpSenderType.EMAIL);
//        ReflectionTestUtils.setField(expiredOtp, "expiresAt", java.time.Instant.now().minusSeconds(60)); // Expired 1 min ago
//
//        when(otpRepository.findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId))
//            .thenReturn(java.util.Optional.of(expiredOtp));
//
//        // When & Then
//        OtpExpiredException exception = assertThrows(
//            OtpExpiredException.class,
//            () -> otpService.verifyOtp(userId, otpCode, purpose, purposeId)
//        );
//
//        assertEquals("OTP has expired", exception.getMessage());
//    }
//
//    @Test
//    void testVerifyOtpInvalid() {
//        // Given
//        String userId = "user123";
//        String correctOtpCode = "123456";
//        String wrongOtpCode = "654321";
//        Otp.OtpPurpose purpose = Otp.OtpPurpose.API_KEY_CREATION;
//        String purposeId = "test-purpose-id";
//
//        Otp validOtp = Otp.generateNew(userId, "test@example.com", purpose, purposeId, Otp.OtpSenderType.EMAIL);
//        when(otpRepository.findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId))
//            .thenReturn(java.util.Optional.of(validOtp));
//
//        // When & Then
//        OtpInvalidException exception = assertThrows(
//            OtpInvalidException.class,
//            () -> otpService.verifyOtp(userId, wrongOtpCode, purpose, purposeId)
//        );
//
//        assertEquals("Invalid OTP code", exception.getMessage());
//    }
//}
