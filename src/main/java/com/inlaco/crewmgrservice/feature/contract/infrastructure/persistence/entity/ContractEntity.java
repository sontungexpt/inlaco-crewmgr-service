package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity;

import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.contract.domain.model.ContractType;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Party;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("contracts")
public class ContractEntity {

  private String title;

  private Party initiator;

  private List<Party> partners;

  private File contractFile;

  private List<File> attachments;

  private List<String> terms;

  private boolean signed = false;

  private ObjectId signedBy;

  private Instant signedAt;

  private Instant activationDate;

  private boolean activated = false;

  private Instant expiredDate;

  private ObjectId templateId;

  private int contractFreezeDelay;

  private ContractType type;
}
