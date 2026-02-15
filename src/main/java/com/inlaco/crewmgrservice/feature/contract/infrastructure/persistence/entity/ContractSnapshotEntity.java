package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractStatus;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import java.time.Instant;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@CompoundIndex(name = "idx_contract_version", def = "{'contractId':1, 'version':1}", unique = true)
@Document("contract_snapshots")
public class ContractSnapshotEntity {

  @Id private String id;

  private ObjectId contractId;

  private ContractType type;

  private ContractStatus status;

  private int version;

  private int schemaVersion;

  // full domain state
  private Map<String, Object> payload;

  // audit
  @CreatedDate private Instant createdAt;
  @CreatedBy private ObjectId createdBy;
}
