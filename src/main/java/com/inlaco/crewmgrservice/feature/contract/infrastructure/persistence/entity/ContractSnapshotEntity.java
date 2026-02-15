package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity;

<<<<<<< Updated upstream
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractStatus;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import java.time.Instant;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
=======
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
>>>>>>> Stashed changes
import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

<<<<<<< Updated upstream
@Data
@Builder
@CompoundIndex(name = "idx_contract_version", def = "{'contractId':1, 'version':1}", unique = true)
@Document("contract_snapshots")
=======
@Getter
@Setter
@NoArgsConstructor
@Document("contract_snapshots")
@CompoundIndex(name = "idx_contract_version", def = "{'contractId':1, 'version':1}", unique = true)
>>>>>>> Stashed changes
public class ContractSnapshotEntity {

  @Id private String id;

  private ObjectId contractId;

<<<<<<< Updated upstream
  private ContractType type;

  private ContractStatus status;

=======
>>>>>>> Stashed changes
  private int version;

  private int schemaVersion;

  // full domain state
<<<<<<< Updated upstream
  private Map<String, Object> payload;
=======
  private String payload;
>>>>>>> Stashed changes

  // audit
  @CreatedDate private Instant createdAt;
  @CreatedBy private ObjectId createdBy;
}
