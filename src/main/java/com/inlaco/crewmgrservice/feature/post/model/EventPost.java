package com.inlaco.crewmgrservice.feature.post.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Schema(description = "Event post")
public class EventPost extends Post {}
