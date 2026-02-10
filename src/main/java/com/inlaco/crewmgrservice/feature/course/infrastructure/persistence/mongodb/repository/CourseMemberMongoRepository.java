package com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.entity.CourseMemberEntity;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseMemberMongoRepository extends MongoRepository<CourseMemberEntity, String> {

  long countByCourseId(ObjectId courseId);

  boolean existsByCourseIdAndUserId(ObjectId courseId, ObjectId userId);

  Optional<CourseMemberEntity> findByCourseIdAndUserId(ObjectId courseId, ObjectId userId);

  List<CourseMemberEntity> findByCourseId(ObjectId courseId);
}
