package com.inlaco.crewmgrservice.feature.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.user.dto.SailorFilterable;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import com.inlaco.crewmgrservice.feature.user.model.User;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

public interface SailorService {

  void makeSailorOfficial(LaborContract contract);

  Page<SailorProfile> getAllSailors(SailorFilterable filterable, Pageable pageable);

  Page<SailorProfile> searchSailors(String query, Criteria filter, Pageable pageable);

  SailorProfile updateSailorProfile(String sailorId, JsonNode patch);

  SailorProfile saveSailorProfile(SailorProfile sailorProfile);

  String generateSailorCardId();

  /**
   * Find sailor profile by id. For user that has role ADMIN, they can view all sailor profiles.
   *
   * @param sailorId the sailor id
   * @return the sailor profile
   */
  SailorProfile findSailorProfileById(String sailorId);

  SailorProfile findSailorProfileByIdOrNull(String sailorId);

  SailorProfile findSailorProfileByAccountId(String accountId);

  SailorProfile findSailorProfileByAccountIdOrNull(String sailorId);

  SailorProfile findSailorProfileByAccountIdOrNull(ObjectId sailorId);

  /**
   * Find current sailor profile of user.
   *
   * @param user the user
   * @return the sailor profile
   */
  SailorProfile findMySailorProfile(User user);

  boolean existsSailorProfileById(String sailorId);

  List<SailorProfile> findSailorProfilesByCardIds(Iterable<String> sailorCardIds);

  SailorProfile findSailorProfileByCardId(String cardId);
}
