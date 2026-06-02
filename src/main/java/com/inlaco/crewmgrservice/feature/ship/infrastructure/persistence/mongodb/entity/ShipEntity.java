package com.inlaco.crewmgrservice.feature.ship.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.feature.ship.domain.model.Ship;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ships")
public class ShipEntity {

  @Id
  private ObjectId id;

  @Field("name")
  private String name;

  @Field("imo_number")
  private String imoNumber;

  @Field("call_sign")
  private String callSign;

  @Field("mmsi")
  private String mmsi;

  @Field("flag")
  private String flag;

  @Field("port_of_registry")
  private String portOfRegistry;

  @Field("ship_type")
  private String shipType;

  @Field("classification_society")
  private String classificationSociety;

  @Field("year_built")
  private Integer yearBuilt;

  @Field("shipyard")
  private String shipyard;

  @Field("deadweight")
  private Double deadweight;

  @Field("gross_tonnage")
  private Double grossTonnage;

  @Field("net_tonnage")
  private Double netTonnage;

  @Field("length_overall")
  private Double lengthOverall;

  @Field("beam")
  private Double beam;

  @Field("draft")
  private Double draft;

  @Field("engine_type")
  private String engineType;

  @Field("engine_power")
  private Double enginePower;

  @Field("fuel_type")
  private String fuelType;

  @Field("maximum_crew_capacity")
  private Integer maximumCrewCapacity;

  @Field("current_crew_count")
  private Integer currentCrewCount;

  @Field("owner_company_id")
  private String ownerCompanyId;

  @Field("operator_company_id")
  private String operatorCompanyId;

  @Field("image")
  private Asset image;

  @Field("documents")
  private Asset documents;

  @Field("description")
  private String description;

  @Field("status")
  private Ship.ShipStatus status;

  @Field("last_inspection_date")
  private LocalDate lastInspectionDate;

  @Field("next_inspection_date")
  private LocalDate nextInspectionDate;

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
