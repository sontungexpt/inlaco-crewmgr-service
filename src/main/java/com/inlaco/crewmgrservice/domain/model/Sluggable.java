package com.inlaco.crewmgrservice.domain.model;

/** Marker interface for aggregate roots or entities that support slug generation. */
public interface Sluggable<ID> {

  ID getId();
}
