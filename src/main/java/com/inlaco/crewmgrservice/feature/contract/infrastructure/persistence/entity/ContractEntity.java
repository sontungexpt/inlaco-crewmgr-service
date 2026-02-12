package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity;

import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.contract.domain.model.ContractType;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Party;
import java.time.Instant;
import java.util.List;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("contracts")
public class ContractEntity {

  private String title;

  private Party initiator;

  private List<Party> partners;

  private File contractFile;

  private List<File> attachments;

  private List<String> terms;

  private boolean signed = false;

  private Instant activationDate;

  private boolean activated = false;

  private Instant expiredDate;

  private int contractFreezeDelay;

  private ContractType type;

  private ObjectId signedBy;

  private Instant signedAt;

  @CreatedBy private ObjectId createdBy;

  @CreatedDate private Instant createdAt;

  @LastModifiedBy private ObjectId updatedBy;

  @LastModifiedDate private Instant updatedAt;
}
