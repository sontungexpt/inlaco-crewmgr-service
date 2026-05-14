package com.inlaco.crewmgrservice.feature.crew.application.port.out;

import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewOperationalStatus;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrewProfileRepository {

  @Builder
  public record CrewProfileSearchCriteria(
      String keyword,
      String professionalPosition,
      Boolean official,
      CrewOperationalStatus workStatus,
      List<String> excludedIds,
      List<String> excludedEmployeeCardIds) {}

  CrewProfile save(CrewProfile profile);

  List<CrewProfile> saveAll(Iterable<CrewProfile> profiles);

  Optional<CrewProfile> findById(String profileId);

  Optional<CrewProfile> findByAccountId(String accountId);

  Optional<CrewProfile> findByEmployeeCardId(String cardId);

  Page<CrewProfile> findAll(Pageable pageable);

  Page<CrewProfile> findAll(
      CrewProfileRepository.CrewProfileSearchCriteria criteria, Pageable pageable);

  List<CrewProfile> findAllById(Iterable<String> ids);

  List<CrewProfile> findAllByAccountId(Iterable<String> accountIds);

  List<CrewProfile> findAllByEmployeeCardId(Iterable<String> cardIds);

  boolean existsAllByEmployeeCardIds(Iterable<String> employeeIds);
}
