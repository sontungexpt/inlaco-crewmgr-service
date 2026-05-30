package com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in;

import com.inlaco.crewmgrservice.feature.crewmobilization.application.model.CrewMobilizationDetail;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.model.CrewMobilizationSearchCriteria;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilization;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilizationAssignment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrewMobilizationQueryUseCase {

  CrewMobilization findMobilization(String id);

  CrewMobilizationDetail findDetailMobilization(String id);

  Page<CrewMobilization> findMobilizations(
      CrewMobilizationSearchCriteria criteria, Pageable pageable);

  List<CrewMobilizationAssignment> findCrewsAssigned(String mobilizationId);

  List<CrewMobilizationAssignment> findAllActiveAssignments();

  Page<CrewMobilizationAssignment> findAllActiveAssignments(Pageable pageable);

  List<CrewMobilizationAssignment> findAllActiveAssignmentsForClient(String clientId);

  Page<CrewMobilizationAssignment> findAllActiveAssignmentsForClient(
      String clientId, Pageable pageable);
}
