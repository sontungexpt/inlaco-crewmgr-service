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
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("contracts")
@CompoundIndexes({
  @CompoundIndex(name = "idx_type_status", def = "{'type':1, 'status':1}"),
  @CompoundIndex(name = "idx_status_activationDate", def = "{'status':1, 'activationDate':1}"),
  @CompoundIndex(name = "idx_status_expiredDate", def = "{'status':1, 'expiredDate':1}"),
  // Index cho meta applicationId
  @CompoundIndex(
      name = "idx_meta_application_status",
      def = "{'searchMeta.applicationId':1, 'status':1}",
      sparse = true)
})
public class ContractEntity {

  @Id private String id;

  private ContractType type;

  private ContractStatus status;

  private Instant activationDate;

  private Instant expiredDate;

  private int version;

  private int schemaVersion;

  /** Completed Json */
  private Map<String, Object> payload;

  // =============================
  // Polymorphic search meta
  // =============================
  private Map<String, Object> searchMeta;

  @CreatedBy private ObjectId createdBy;
  @CreatedDate private Instant createdAt;
  @LastModifiedBy private ObjectId updatedBy;
  @LastModifiedDate private Instant updatedAt;
}
