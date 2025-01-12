package com.inlaco.crewmgrservice.feature.course.repository;

import com.inlaco.crewmgrservice.feature.course.model.CourseMember;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseMemberRepository extends MongoRepository<CourseMember, String> {

  long countByCourseId(ObjectId courseId);

  boolean existsByCourseIdAndUserId(ObjectId courseId, ObjectId userId);

  Optional<CourseMember> findByCourseIdAndUserId(ObjectId courseId, ObjectId userId);

  List<CourseMember> findByCourseId(ObjectId courseId);
}
