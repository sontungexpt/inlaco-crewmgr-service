package com.inlaco.crewmgrservice.feature.attendance.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.attendance.infrastructure.persistence.mongodb.entity.AttendanceEntity;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceMongoRepository extends MongoRepository<AttendanceEntity, String> {

  Optional<AttendanceEntity> findByUserIdAndScheduleId(ObjectId userId, ObjectId scheduleId);

  boolean existsByUserIdAndScheduleId(ObjectId userId, ObjectId scheduleId);
}
