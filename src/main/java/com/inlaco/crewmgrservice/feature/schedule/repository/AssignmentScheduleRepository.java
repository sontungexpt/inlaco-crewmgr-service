package com.inlaco.crewmgrservice.feature.schedule.repository;

import com.inlaco.crewmgrservice.feature.schedule.model.AssigmentSchedule;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentScheduleRepository extends MongoRepository<AssigmentSchedule, String> {

  List<AssigmentSchedule> findByCrewMembersCardIdContains(String cardId);
}
