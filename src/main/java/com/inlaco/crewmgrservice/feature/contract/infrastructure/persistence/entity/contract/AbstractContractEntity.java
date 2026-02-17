package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity.contract;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractStatus;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import com.inlaco.crewmgrservice.feature.contract.domain.model.party.Party;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.ContractStatusHistory;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.DynamicAttribute;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.time.Instant;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
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
public abstract class AbstractContractEntity {

  @Id private String id;

  private ContractType type;
  private ContractStatus status;

  private Instant activationDate;
  private Instant expiredDate;

  private int version;

  // ===== BASIC INFO =====
  private String title;
  private Party initiator;
  private List<Party> partners;
  private Asset contractFile;
  private List<Asset> attachments;
  private List<String> terms;
  private List<DynamicAttribute> customAttributes;

  private List<ContractStatusHistory> statusHistories;
}
