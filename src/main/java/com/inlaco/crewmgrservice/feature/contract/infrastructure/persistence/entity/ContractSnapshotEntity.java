package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractStatus;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.Version;
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
@Document("contract_snapshots")
@CompoundIndex(name = "idx_contract_version", def = "{'contractId':1, 'version':1}", unique = true)
public class ContractSnapshotEntity {

  @Id private String id;

  private ObjectId contractId;
  private Version version;

  private ContractType type;
  private ContractStatus status;

  private int schemaVersion;

  private Map<String, Object> searchMeta;

  // full aggregate serialized
  private Map<String, Object> payload;

  @CreatedDate private Instant createdAt;
  @CreatedBy private ObjectId createdBy;
}
