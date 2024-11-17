package com.inlaco.crewmgrservice.feature.user.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@SuperBuilder
@Getter
@Setter
@Document(collection = "sailors")
@NoArgsConstructor
@AllArgsConstructor
public class Sailor extends Person {

  @CreatedDate private Instant joinedAt;

  @LastModifiedDate private Instant updatedAt;
}
