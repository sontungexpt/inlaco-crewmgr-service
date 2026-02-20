package com.inlaco.crewmgrservice.feature.post.presentation.dto.request.update;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.inlaco.crewmgrservice.feature.post.domain.enums.PostType;
import com.inlaco.crewmgrservice.shared.application.model.Patch;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;

@Data
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
    /* visible = true */ )
@JsonSubTypes({
  @Type(value = RecruitmentPostPatchRequest.class, name = PostType.Fields.RECRUITMENT),
})
public class PostPatchRequest {
  Patch<@NotBlank String> title = Patch.unchanged();
  Patch<@NotBlank String> content = Patch.unchanged();
  Patch<String> description = Patch.unchanged();
  Patch<List<String>> attachments = Patch.unchanged();
  Patch<@NotBlank String> image = Patch.unchanged();
  Patch<@NotBlank String> company = Patch.unchanged();
}
