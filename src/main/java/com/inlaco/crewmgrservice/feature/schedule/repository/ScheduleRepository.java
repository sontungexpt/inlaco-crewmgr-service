package com.inlaco.crewmgrservice.feature.schedule.repository;

import com.inlaco.crewmgrservice.feature.schedule.model.Schedule;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {

  List<Schedule> findByCrewMembersCardIdContains(String cardId);
}
