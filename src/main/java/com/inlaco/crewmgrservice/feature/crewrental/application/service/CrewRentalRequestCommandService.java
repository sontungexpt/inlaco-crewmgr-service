package com.inlaco.crewmgrservice.feature.crewrental.application.service;

import com.inlaco.crewmgrservice.feature.crewrental.application.port.in.CrewRentalRequestCommandUseCase;
import com.inlaco.crewmgrservice.feature.crewrental.application.port.out.CrewRentalRequestRepository;
import com.inlaco.crewmgrservice.feature.crewrental.domain.enums.CrewRentalRequestStatus;
import com.inlaco.crewmgrservice.feature.crewrental.domain.model.CrewRentalRequest;
import com.inlaco.crewmgrservice.feature.upload.application.enums.UploadStrategy;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadFactory;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrewRentalRequestCommandService implements CrewRentalRequestCommandUseCase {
  private final CrewRentalRequestRepository crewRentalRequestRepository;
  private final UploadFactory uploadFactory;

  @Override
  public void review(String requestId, boolean accepted, User reviewer) {
    CrewRentalRequest request =
        crewRentalRequestRepository
            .findById(requestId)
            .orElseThrow(
                () -> new ResourceNotFoundException(CrewRentalRequest.class, "id", requestId));

    request.setReviewedBy(reviewer.getId());
    request.setStatus(
        accepted ? CrewRentalRequestStatus.APPROVED : CrewRentalRequestStatus.REJECTED);
    crewRentalRequestRepository.save(request);
  }

  @Override
  public CrewRentalRequest create(
      CrewRentalRequest request, String detailFileAssetId, String shipImageAssetId) {
    request.setDetailFile(
        uploadFactory.metadata(UploadStrategy.CREW_RENTAL_REQUEST_DETAIL_FILE, detailFileAssetId));
    request
        .getShipInfo()
        .setImage(uploadFactory.metadata(UploadStrategy.SHIP_IMAGE, shipImageAssetId));
    return crewRentalRequestRepository.save(request);
  }

  @Override
  public CrewRentalRequest markSigning(CrewRentalRequest request, String contractId) {
    request.setContractId(contractId);
    request.setStatus(CrewRentalRequestStatus.SIGNING);
    return crewRentalRequestRepository.save(request);
  }
}
