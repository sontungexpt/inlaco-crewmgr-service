package com.inlaco.crewmgrservice.feature.user.repository;

import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateProfileRepository extends MongoRepository<CandidateProfile, String> {

  List<CandidateProfile> findByAccountId(ObjectId accountId);

  Page<CandidateProfile> findByStatus(CandidateProfile.Status status, Pageable pageable);
}
