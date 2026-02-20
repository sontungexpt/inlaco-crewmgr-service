package com.inlaco.crewmgrservice.feature.post.presentation.mapper;

import com.inlaco.crewmgrservice.feature.post.domain.model.EventPost;
import com.inlaco.crewmgrservice.feature.post.domain.model.NewsPost;
import com.inlaco.crewmgrservice.feature.post.domain.model.Post;
import com.inlaco.crewmgrservice.feature.post.domain.model.PostUpdateCommand;
import com.inlaco.crewmgrservice.feature.post.domain.model.RecruitmentPost;
import com.inlaco.crewmgrservice.feature.post.domain.model.RecruitmentPostUpdateCommand;
import com.inlaco.crewmgrservice.feature.post.presentation.dto.EventPostDTO;
import com.inlaco.crewmgrservice.feature.post.presentation.dto.NewsPostDTO;
import com.inlaco.crewmgrservice.feature.post.presentation.dto.PostDTO;
import com.inlaco.crewmgrservice.feature.post.presentation.dto.RecruitmentPostDTO;
import com.inlaco.crewmgrservice.feature.post.presentation.dto.request.update.PostPatchRequest;
import com.inlaco.crewmgrservice.feature.post.presentation.dto.request.update.RecruitmentPostPatchRequest;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadDispatcher;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.shared.application.model.Patch;
import com.inlaco.crewmgrservice.shared.mapstruct.mapper.AssetResponseMapper;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
    componentModel = "spring",
    subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION,
    uses = {AssetResponseMapper.class},
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class PostMapper {

  @Autowired private UploadDispatcher uploadDispatcher;

  // ===== DOMAIN → DTO =====

  @SubclassMapping(source = NewsPost.class, target = NewsPostDTO.class)
  @SubclassMapping(source = RecruitmentPost.class, target = RecruitmentPostDTO.class)
  @SubclassMapping(source = EventPost.class, target = EventPostDTO.class)
  public abstract PostDTO toPostDTO(Post post);

  // ===== DTO → DOMAIN =====

  @SubclassMapping(source = NewsPostDTO.class, target = NewsPost.class)
  @SubclassMapping(source = RecruitmentPostDTO.class, target = RecruitmentPost.class)
  @SubclassMapping(source = EventPostDTO.class, target = EventPost.class)
  @Mapping(
      target = "attachments",
      source = "attachmentAssetIds",
      qualifiedByName = "mapIdsToAttachments")
  @Mapping(target = "image", source = "imageAssetId", qualifiedByName = "mapImageAssetIdToAsset")
  public abstract Post toPost(PostDTO dto);

  @Mapping(target = "image", source = "image", qualifiedByName = "toAssetImagePatch")
  @Mapping(
      target = "attachments",
      source = "attachments",
      qualifiedByName = "toAssetAttachmentsPatch")
  @SubclassMapping(
      source = RecruitmentPostPatchRequest.class,
      target = RecruitmentPostUpdateCommand.class)
  public abstract PostUpdateCommand toPostUpdateCommand(PostPatchRequest patchRequest);

  @Named("toAssetImagePatch")
  public Patch<Asset> toAssetImagePatch(Patch<String> patch) {
    return patch.map(assetId -> mapImageAssetIdToAsset(assetId));
  }

  @Named("toAssetAttachmentsPatch")
  public Patch<List<Asset>> toAssetAttachmentsPatch(Patch<List<String>> patch) {
    return patch.map(
        assetIds ->
            assetIds.stream()
                .map(assetId -> uploadDispatcher.fetch(AssetType.POST_ATTACHMENT, assetId))
                .toList());
  }

  @Named("mapIdsToAttachments")
  protected List<Asset> mapIdsToAttachments(List<String> ids) {
    if (ids == null) return null;
    return ids.stream()
        .map(assetId -> uploadDispatcher.fetch(AssetType.POST_ATTACHMENT, assetId))
        .toList();
  }

  @Named("mapImageAssetIdToAsset")
  protected Asset mapImageAssetIdToAsset(String assetId) {
    if (assetId == null) return null;
    return uploadDispatcher.fetch(AssetType.POST_IMAGE, assetId);
  }
}
