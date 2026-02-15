package com.inlaco.crewmgrservice.shared.application.port.out;

public interface SequenceGenerator {
  long next(String key);
}
