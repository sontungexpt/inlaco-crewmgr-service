//package com.inlaco.crewmgrservice.feature.totp.presentation.rest.controller;
//
//import com.inlaco.crewmgrservice.feature.totp.domain.exception.TotpDisabledException;
//import com.inlaco.crewmgrservice.feature.totp.domain.exception.TotpInvalidCodeException;
//import com.inlaco.crewmgrservice.feature.totp.domain.exception.TotpNotFoundException;
//import com.inlaco.crewmgrservice.feature.totp.domain.exception.TotpSetupFailedException;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/test")
//public class TestTotpExceptionController {
//
//    @PostMapping("/totp-not-found")
//    public void throwTotpNotFoundException() {
//        throw new TotpNotFoundException("TOTP not found for this request");
//    }
//
//    @PostMapping("/totp-disabled")
//    public void throwTotpDisabledException() {
//        throw new TotpDisabledException("TOTP is disabled for this purpose");
//    }
//
//    @PostMapping("/totp-invalid-code")
//    public void throwTotpInvalidCodeException() {
//        throw new TotpInvalidCodeException("Invalid TOTP verification code");
//    }
//
//    @PostMapping("/totp-setup-failed")
//    public void throwTotpSetupFailedException() {
//        throw new TotpSetupFailedException("Failed to setup TOTP configuration");
//    }
//}
