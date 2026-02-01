package com.inlaco.crewmgrservice.feature.schedule.repository;

import com.inlaco.crewmgrservice.feature.schedule.model.AssignedMobilization;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentScheduleRepository
    extends MongoRepository<AssignedMobilization, String> {

  List<AssignedMobilization> findByCrewMembersCardIdContains(String cardId);
}
