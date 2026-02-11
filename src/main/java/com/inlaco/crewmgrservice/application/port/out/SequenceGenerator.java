package com.inlaco.crewmgrservice.application.port.out;

public interface SequenceGenerator {
  long next(String key);
}
