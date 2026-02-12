package com.inlaco.crewmgrservice.feature.crew.application.port.out;

import com.inlaco.crewmgrservice.feature.crew.application.model.CrewProfileSearchCriteria;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrewProfileRepository {

  CrewProfile save(CrewProfile profile);

  Optional<CrewProfile> findById(String profileId);

  Optional<CrewProfile> findByAccountId(String accountId);

  Optional<CrewProfile> findByEmployeeCardId(String cardId);

  Page<CrewProfile> findAll(Pageable pageable);

  Page<CrewProfile> findAll(@Nullable CrewProfileSearchCriteria criteria, Pageable pageable);

  List<CrewProfile> findByIdIn(Iterable<String> ids);

  List<CrewProfile> findByEmployeeCardIdIn(Iterable<String> cardIds);
}
