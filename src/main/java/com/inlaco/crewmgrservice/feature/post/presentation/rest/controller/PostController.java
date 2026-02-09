package com.inlaco.crewmgrservice.feature.post.presentation.rest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.post.application.port.in.PostUseCase;
import com.inlaco.crewmgrservice.feature.post.presentation.dto.PostDTO;
import com.inlaco.crewmgrservice.feature.post.presentation.dto.enums.PostType;
import com.inlaco.crewmgrservice.feature.post.presentation.mapper.PostMapper;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.PageableQueryParams;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.PublicEndpoint;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@PublicEndpoint
@RequiredArgsConstructor
@Tag(name = "Post", description = "A collection endpoints to work with posts")
public class PostController {

  private final PostUseCase postUseCase;
  private final PostMapper postMapper;

  @Operation(summary = "Retrieve a post from the server by id")
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @PublicEndpoint
  public PostDTO getPost(@PathVariable("id") String id) {
    return postMapper.toDTO(postUseCase.getPost(id));
  }

  @Operation(
      summary = "Create a new post with the given data using type field to identify the post type",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  @PublicEndpoint(profiles = "dev")
  public PostDTO createPost(@CurrentUser User user, @RequestBody @Valid PostDTO newPost) {
    return postMapper.toDTO(postUseCase.createPost(postMapper.toDomain(newPost), user));
  }

  @Operation(
      summary = "Update a post with post Id",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
  @ResponseStatus(HttpStatus.OK)
  public PostDTO updatePost(
      @CurrentUser User user, @PathVariable("id") String id, @RequestBody JsonNode patch) {
    return postMapper.toDTO(postUseCase.updatePost(id, patch, user));
  }

  @Operation(
      summary = "Delete a post with post Id",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletePost(@CurrentUser User user, @PathVariable("id") String id) {
    postUseCase.deletePost(id, user);
  }

  @Operation(summary = "Retrieve all posts for a given page")
  @GetMapping("/web")
  @ResponseStatus(HttpStatus.OK)
  @PageableQueryParams
  public Page<PostDTO> getAllPosts(
      @RequestParam(required = false) PostType type,
      @PageableDefault(size = 10, page = 0) Pageable pageable) {
    return postUseCase.getPagePosts(pageable, type).map(postMapper::toDTO);
  }
}
