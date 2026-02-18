package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity.contract;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractStatus;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import com.inlaco.crewmgrservice.feature.contract.domain.model.party.Party;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.ContractStatusHistory;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.DynamicAttribute;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.Version;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.time.Instant;
import java.util.List;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("contracts")
@CompoundIndex(name = "idx_type_status", def = "{'type':1, 'status':1}")
@CompoundIndex(name = "idx_status_activationDate", def = "{'status':1, 'activationDate':1}")
@CompoundIndex(name = "idx_status_expiredDate", def = "{'status':1, 'expiredDate':1}")
public abstract class ContractEntity {

  @Id private String id;
  private final ContractType type;

  protected ContractEntity(ContractType type) {
    this.type = type;
  }

  // ===== BASIC INFO =====
  private String title;
  private Party initiator;
  private List<Party> partners;
  private Asset contractFile;
  private List<Asset> attachments;
  private Version version;
  private List<DynamicAttribute> customAttributes;

  // STATUS
  private ContractStatus status;
  private List<ContractStatusHistory> statusHistories;

  // TIME
  private Instant activationDate;
  private Instant expiredDate;
  private int contractFreezeDelayMinutes;

  private int schemaVersion;

  // Audit
  @CreatedBy private ObjectId createdBy;
  @LastModifiedBy private ObjectId updatedBy;
  @CreatedDate private Instant createdAt;
  @LastModifiedDate private Instant updatedAt;
}
