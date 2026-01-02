package com.inlaco.crewmgrservice.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class File implements Serializable {

  private String publicId;

  @JsonIgnore private String assetId;

  private String url;

  private String displayName;

  private String resourceType;

  private String format;

  private Long bytes;

  private Instant uploadedAt;
}
