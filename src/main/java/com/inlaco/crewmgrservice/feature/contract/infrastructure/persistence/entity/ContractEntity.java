package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractStatus;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import java.time.Instant;
<<<<<<< Updated upstream
import java.util.Map;
import lombok.Builder;
import lombok.Data;
=======
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
>>>>>>> Stashed changes
import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

<<<<<<< Updated upstream
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
=======
@Document("contracts")
@CompoundIndexes({

  // Query contract theo type + status
  @CompoundIndex(name = "idx_type_status", def = "{'type':1, 'status':1}"),

  // Timer activate (SIGNED -> ACTIVE)
  @CompoundIndex(name = "idx_status_activationDate", def = "{'status':1, 'activationDate':1}"),

  // Timer expire (ACTIVE -> EXPIRED)
  @CompoundIndex(name = "idx_status_expiredDate", def = "{'status':1, 'expiredDate':1}"),

  // Query theo partner + status
  @CompoundIndex(name = "idx_partner_status", def = "{'mainPartnerId':1, 'status':1}")
})
@NoArgsConstructor
@Getter
@Setter
>>>>>>> Stashed changes
public class ContractEntity {

  @Id private String id;

  private ContractType type;

<<<<<<< Updated upstream
  private ContractStatus status;
=======
  /** Single source of truth for lifecycle */
  private ContractStatus status;

  /** Activation trigger */
  private Instant activationDate;

  /** Expiration trigger */
  private Instant expiredDate;

  /** Main partner for quick filtering */
  private String mainPartnerId;

  /** Optimistic locking */
  @Version private Long version;

  /** For schema evolution */
  private int schemaVersion;

  /** Snapshot of aggregate */
  private String payload;

  /** Audit for SIGNED action */
  private ObjectId signedBy;
>>>>>>> Stashed changes

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

  // ========================
  // Spring auditing
  // ========================

  @CreatedBy private ObjectId createdBy;
  @CreatedDate private Instant createdAt;
  @LastModifiedBy private ObjectId updatedBy;
  @LastModifiedDate private Instant updatedAt;
}
