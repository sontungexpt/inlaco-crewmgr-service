package com.inlaco.crewmgrservice.common.model;

public interface Versionable<ID, VT> {

  ID getId();

  VT getVersion();

  VT getPrevVersion();

  String getChangeLog();
}
