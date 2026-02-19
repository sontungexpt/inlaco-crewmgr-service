package com.inlaco.crewmgrservice.feature.post.domain.model;

import com.inlaco.crewmgrservice.shared.application.model.Patch;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.util.List;
import lombok.Data;

@Data
public class PostUpdateCommand {
  Patch<String> title;
  Patch<String> content;
  Patch<String> description;
  Patch<List<Asset>> attachments;
  Patch<Asset> image;
  Patch<String> company;
}
