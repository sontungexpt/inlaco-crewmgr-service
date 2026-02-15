package com.inlaco.crewmgrservice.feature.contracttemplate.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.common.model.Asset;
import java.time.Instant;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("contract_templates")
public class ContractTemplateEntity {

  @Id private String id;

  private String name;

  private String description;

  private Asset metadata;

  private String type;

  @CreatedBy private ObjectId createdBy;

  @CreatedDate private Instant createdAt;

  @LastModifiedBy private Instant updatedAt;

  @LastModifiedDate private ObjectId updatedBy;
}
