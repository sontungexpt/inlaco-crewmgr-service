package com.inlaco.crewmgrservice.feature.crewrental.application.service;

import com.inlaco.crewmgrservice.feature.crewrental.application.port.in.CrewRentalRequestCommandUseCase;
import com.inlaco.crewmgrservice.feature.crewrental.application.port.out.CrewRentalRequestRepository;
import com.inlaco.crewmgrservice.feature.crewrental.domain.enums.CrewRentalRequestStatus;
import com.inlaco.crewmgrservice.feature.crewrental.domain.model.CrewRentalRequest;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadDispatcher;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
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
  private final UploadDispatcher uploadDispatcher;

  @Override
  public void review(String requestId, boolean accepted, User reviewer) {
    log.debug("Reviewing crew rental request with ID: {}", requestId);
    CrewRentalRequest request =
        crewRentalRequestRepository
            .findById(requestId)
            .orElseThrow(
                () -> {
                  log.warn("Crew rental request not found with ID: {}", requestId);
                  return new ResourceNotFoundException(CrewRentalRequest.class, "id", requestId);
                });

    request.setReviewedBy(reviewer.getId());
    request.setStatus(
        accepted ? CrewRentalRequestStatus.APPROVED : CrewRentalRequestStatus.REJECTED);
    crewRentalRequestRepository.save(request);
    log.info(
        "Crew rental request with ID: {} reviewed and marked as {}",
        requestId,
        request.getStatus());
  }

  @Override
  public CrewRentalRequest create(
      CrewRentalRequest request, String detailFileAssetId, String shipImageAssetId) {
    log.debug("Fetching detail file for crew rental request with ID: {}", request.getId());
    request.setDetailFile(
        uploadDispatcher.fetch(AssetType.CREW_RENTAL_REQUEST_DETAIL_FILE, detailFileAssetId));
    log.debug("Fetching ship image for crew rental request with ID: {}", request.getId());
    request.getShipInfo().setImage(uploadDispatcher.fetch(AssetType.SHIP_IMAGE, shipImageAssetId));
    CrewRentalRequest savedRequest = crewRentalRequestRepository.save(request);
    log.info("Crew rental request created with ID: {}", savedRequest.getId());
    return savedRequest;
  }

  @Override
  public CrewRentalRequest markSigning(CrewRentalRequest request, String contractId) {
    request.setContractId(contractId);
    request.setStatus(CrewRentalRequestStatus.SIGNING);
    CrewRentalRequest updatedRequest = crewRentalRequestRepository.save(request);
    log.info(
        "Crew rental request with ID: {} marked as SIGNING with contract ID: {}",
        updatedRequest.getId(),
        contractId);
    return updatedRequest;
  }
}
