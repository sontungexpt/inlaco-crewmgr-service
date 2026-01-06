package com.inlaco.crewmgrservice.common.model;

import com.inlaco.crewmgrservice.annotation.JsonPatchIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Auditable {

  @CreatedDate
  @Schema(hidden = true)
  @JsonPatchIgnore
  protected Instant createdAt;

  @Schema(hidden = true)
  @LastModifiedDate
  @JsonPatchIgnore
  protected Instant updatedAt;
}
