package com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.AttendanceMethod;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.CheckType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
    /* visible = true */ )
@JsonSubTypes({
  @Type(value = VerifyQRAttendanceRequest.class, name = AttendanceMethod.Fields.QR_CODE),
})
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class VerifyAttendanceRequest {

  @NotBlank private String location;

  @NotNull private CheckType checkType;

  @NotNull private AttendanceMethod type;
}
