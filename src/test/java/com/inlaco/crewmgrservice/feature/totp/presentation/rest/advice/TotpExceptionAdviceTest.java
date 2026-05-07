//package com.inlaco.crewmgrservice.feature.totp.presentation.rest.advice;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.inlaco.crewmgrservice.feature.totp.domain.exception.TotpDisabledException;
//import com.inlaco.crewmgrservice.feature.totp.domain.exception.TotpInvalidCodeException;
//import com.inlaco.crewmgrservice.feature.totp.domain.exception.TotpNotFoundException;
//import com.inlaco.crewmgrservice.feature.totp.domain.exception.TotpSetupFailedException;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureWebMvc
//class TotpExceptionAdviceTest {
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private MockMvc mockMvc;
//
//    @Test
//    void testTotpNotFoundExceptionHandling() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//
//        mockMvc.perform(post("/test/totp-not-found")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.errorCode").value("TOTP_NOT_FOUND"))
//                .andExpect(jsonPath("$.message").exists());
//    }
//
//    @Test
//    void testTotpDisabledExceptionHandling() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//
//        mockMvc.perform(post("/test/totp-disabled")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isForbidden())
//                .andExpect(jsonPath("$.errorCode").value("TOTP_DISABLED"))
//                .andExpect(jsonPath("$.message").exists());
//    }
//
//    @Test
//    void testTotpInvalidCodeExceptionHandling() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//
//        mockMvc.perform(post("/test/totp-invalid-code")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.errorCode").value("TOTP_INVALID_CODE"))
//                .andExpect(jsonPath("$.message").exists());
//    }
//
//    @Test
//    void testTotpSetupFailedExceptionHandling() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//
//        mockMvc.perform(post("/test/totp-setup-failed")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(jsonPath("$.errorCode").value("TOTP_SETUP_FAILED"))
//                .andExpect(jsonPath("$.message").exists());
//    }
//}
