package com.inlaco.crewmgrservice.feature.post.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.inlaco.crewmgrservice.feature.post.enums.PostType;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonTypeName(PostType.Fields.NEWS)
@SuperBuilder
@NoArgsConstructor
public class NewsPost extends Post {}
