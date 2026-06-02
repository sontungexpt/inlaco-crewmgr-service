package com.inlaco.crewmgrservice.feature.company.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.feature.company.domain.model.Company;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "companies")
public class CompanyEntity {

  @Id
  private ObjectId id;

  @Field("name")
  private String name;

  @Field("description")
  private String description;

  @Field("registration_number")
  private String registrationNumber;

  @Field("tax_id")
  private String taxId;

  @Field("address")
  private String address;

  @Field("phone_number")
  private String phoneNumber;

  @Field("email")
  private String email;

  @Field("website")
  private String website;

  @Field("logo")
  private Asset logo;

  @Field("status")
  private Company.CompanyStatus status;

  @Field("created_by")
  private String createdBy;

  @Field("created_at")
  private Long createdAt;

  @Field("updated_by")
  private String updatedBy;

  @Field("updated_at")
  private Long updatedAt;

  public String getIdAsString() {
    return id != null ? id.toHexString() : null;
  }
}
