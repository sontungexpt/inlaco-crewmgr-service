package com.inlaco.crewmgrservice.infrastructure.persistence.mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "__sequences")
public class SequenceEntity {
  @Id private String id;
  private long seq;
}
