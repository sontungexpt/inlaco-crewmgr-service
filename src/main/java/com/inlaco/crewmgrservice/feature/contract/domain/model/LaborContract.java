package com.inlaco.crewmgrservice.feature.contract.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.patch.JsonPatchIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter
@SuperBuilder
public class LaborContract extends DynamicContract {

  public LaborContract() {
    super(ContractType.LABOR_CONTRACT);
  }

  @Schema(description = "Employee ID", hidden = true)
  @JsonIgnore
  @JsonPatchIgnore
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId employeeId;

  @Schema(description = "candidate profile ID", hidden = true)
  @JsonIgnore
  @JsonPatchIgnore
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId candidateProfileId;

  @Schema(description = "The position of the employee", example = "Engineer")
  @NotBlank
  private String position;

  @Schema(description = "The working location of the employee", example = "Hanoi")
  @NotBlank
  private String workingLocation;

  @Schema(description = "The basic salary of the employee", example = "1000000")
  @NotBlank
  private String basicSalary;

  @Schema(description = "The allowance of the employee", example = "1000000")
  private String allowance;

  @Schema(description = "The receive method of the employee", example = "Bank")
  private String receiveMethod;

  @Schema(description = "The payday of the employee", example = "2022-01-01")
  private String payday;

  @Schema(description = "The salary review period of the employee", example = "2022-01-01")
  private String salaryReviewPeriod;
}
