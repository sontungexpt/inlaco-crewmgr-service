package com.inlaco.crewmgrservice.feature.crewrental.service;

import com.inlaco.crewmgrservice.feature.crewrental.dto.RentalRequestFilterable;
import com.inlaco.crewmgrservice.feature.crewrental.model.RentalRequest;
import com.inlaco.crewmgrservice.feature.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RentalRequestService {

  RentalRequest createRequest(
      RentalRequest request, String detailFileAssetId, String shipImageAssetId);

  RentalRequest getRequestById(String requestId);

  RentalRequest saveRequest(RentalRequest request);

  void reviewRequest(String requestId, boolean accepted, User reviewer);

  Page<RentalRequest> searchRequests(
      String query, RentalRequestFilterable filterable, Pageable pageable);

  Page<RentalRequest> findAllRequests(RentalRequestFilterable filterable, Pageable pageable);
}
