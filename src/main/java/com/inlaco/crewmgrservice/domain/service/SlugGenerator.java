package com.inlaco.crewmgrservice.domain.service;

public interface SlugGenerator {

  /**
   * Generate slug for an entity
   *
   * @param entity current entity
   * @param oldEntity previous entity (nullable)
   */
  void generate(Object entity, Object oldEntity);
}
