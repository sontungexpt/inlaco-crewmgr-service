package com.inlaco.crewmgrservice.feature.user.repository;

import com.inlaco.crewmgrservice.feature.user.model.User;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findByUsername(String username);

  Optional<User> findByPubId(String pubId);

  boolean existsByUsername(String username);
}
