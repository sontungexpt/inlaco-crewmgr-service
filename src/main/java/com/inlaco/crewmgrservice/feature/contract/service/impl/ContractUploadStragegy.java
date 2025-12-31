// package com.inlaco.crewmgrservice.feature.contract.service.impl;

// import com.inlaco.crewmgrservice.feature.contract.service.ContractService;
// import com.inlaco.crewmgrservice.feature.upload.dto.UploadOptions;
// import com.inlaco.crewmgrservice.feature.upload.dto.UploadToken;
// import com.inlaco.crewmgrservice.feature.upload.dto.UploadType;
// import com.inlaco.crewmgrservice.feature.upload.enums.UploadStragegy;
// import com.inlaco.crewmgrservice.feature.upload.repository.UploadTokenRepository;
// import com.inlaco.crewmgrservice.feature.upload.service.CloudinaryService;
// import com.inlaco.crewmgrservice.feature.upload.service.UploadServiceStragegy;
// import java.util.List;
// import org.springframework.stereotype.Service;

// @Service(UploadStragegy.Fields.CONTRACT)
// public class ContractUploadStragegy extends UploadServiceStragegy {

//   private final ContractService contractService;

//   public ContractUploadStragegy(
//       ContractService contractService,
//       UploadTokenRepository uploadTokenRepository,
//       CloudinaryService cloudinaryService) {
//     super(uploadTokenRepository, cloudinaryService);
//     this.contractService = contractService;
//   }

//   @Override
//   public UploadOptions getUploadOptions(UploadType type, String id) {
//     // var strategy = type.getStragegy();

//     return null;
//   }

//   @Override
//   public void uploadFile(List<UploadToken> uploadTokens, UploadType nestedType) {
//     // TODO Auto-generated method stub
//     throw new UnsupportedOperationException("Unimplemented method 'uploadFile'");
//   }
// }
