package com.inlaco.crewmgrservice.feature.user.application.port.out;

import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import java.util.Optional;

public interface UserRepository {
  Optional<User> findById(String id);

  Optional<User> findByUsername(String username);

  Optional<User> findByPubId(String pubId);

  boolean existsByUsername(String username);

  User save(User user);
}
