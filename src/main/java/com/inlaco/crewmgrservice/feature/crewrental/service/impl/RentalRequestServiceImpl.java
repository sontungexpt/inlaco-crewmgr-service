package com.inlaco.crewmgrservice.feature.crewrental.service.impl;

import com.inlaco.crewmgrservice.application.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.crewrental.dto.RentalRequestFilterable;
import com.inlaco.crewmgrservice.feature.crewrental.enums.RentalRequestStatus;
import com.inlaco.crewmgrservice.feature.crewrental.model.RentalRequest;
import com.inlaco.crewmgrservice.feature.crewrental.repository.CustomRentalRequestRepository;
import com.inlaco.crewmgrservice.feature.crewrental.repository.RentalRequestRepository;
import com.inlaco.crewmgrservice.feature.crewrental.service.RentalRequestService;
import com.inlaco.crewmgrservice.feature.upload.application.enums.UploadStrategy;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadFactory;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RentalRequestServiceImpl implements RentalRequestService {
  private final CustomRentalRequestRepository customRentalRequestRepository;
  private final RentalRequestRepository rentalRequestRepository;
  private final UploadFactory uploadFactory;

  @Override
  public RentalRequest getRequestById(String requestId) {
    return rentalRequestRepository
        .findById(requestId)
        .orElseThrow(() -> new ResourceNotFoundException(RentalRequest.class, "id", requestId));
  }

  @Override
  public Page<RentalRequest> searchRequests(
      String query, RentalRequestFilterable filterable, Pageable pageable) {
    return customRentalRequestRepository.searchRequests(query, filterable, pageable);
  }

  @Override
  public void reviewRequest(String requestId, boolean accepted, User reviewer) {
    RentalRequest request = getRequestById(requestId);
    request.setReviewedBy(new ObjectId(reviewer.getId()));
    request.setStatus(accepted ? RentalRequestStatus.APPROVED : RentalRequestStatus.REJECTED);
    rentalRequestRepository.save(request);
  }

  @Override
  public Page<RentalRequest> findAllRequests(
      RentalRequestFilterable filterable, Pageable pageable) {
    return customRentalRequestRepository.findAllRequests(filterable, pageable);
  }

  @Override
  public RentalRequest saveRequest(RentalRequest request) {
    return rentalRequestRepository.save(request);
  }

  @Override
  public RentalRequest createRequest(
      RentalRequest request, String detailFileAssetId, String shipImageAssetId) {

    request.setDetailFile(
        uploadFactory.metadata(UploadStrategy.CREW_RENTAL_REQUEST_DETAIL_FILE, detailFileAssetId));

    request
        .getShipInfo()
        .setImage(uploadFactory.metadata(UploadStrategy.SHIP_IMAGE, shipImageAssetId));
    return rentalRequestRepository.save(request);
  }
}
