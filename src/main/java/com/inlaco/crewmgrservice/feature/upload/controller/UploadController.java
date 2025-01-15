package com.inlaco.crewmgrservice.feature.upload.controller;

import com.inlaco.crewmgrservice.feature.upload.service.UploadFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
@Tag(name = "Upload", description = "Upload API")
public class UploadController {

  private final UploadFactory uploadFactory;
}
