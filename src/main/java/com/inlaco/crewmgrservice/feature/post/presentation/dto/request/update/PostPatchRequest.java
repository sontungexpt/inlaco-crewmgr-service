package com.inlaco.crewmgrservice.feature.post.presentation.dto.request.update;

import com.inlaco.crewmgrservice.shared.application.model.Patch;
import java.util.List;
import lombok.Data;

@Data
public class PostPatchRequest {
  Patch<String> title = Patch.unchanged();
  Patch<String> content = Patch.unchanged();
  Patch<String> description = Patch.unchanged();
  Patch<List<String>> attachments = Patch.unchanged();
  Patch<String> image = Patch.unchanged();
  Patch<String> company = Patch.unchanged();
}
