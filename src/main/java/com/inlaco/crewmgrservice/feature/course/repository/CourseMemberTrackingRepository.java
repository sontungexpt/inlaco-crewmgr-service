package com.inlaco.crewmgrservice.feature.course.repository;

import com.inlaco.crewmgrservice.feature.course.model.CourseMemberTracking;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseMemberTrackingRepository
    extends MongoRepository<CourseMemberTracking, String> {

  Optional<CourseMemberTracking> findByCourseIdAndUserId(ObjectId courseId, ObjectId userId);

  List<CourseMemberTracking> findByCourseId(ObjectId courseId);
}
