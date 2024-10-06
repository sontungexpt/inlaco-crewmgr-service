package com.inlaco.crewmgrservice.feature.post.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.annotation.BearerToken;
import com.inlaco.crewmgrservice.annotation.PublicEndpoint;
import com.inlaco.crewmgrservice.config.OpenApiConfig;
import com.inlaco.crewmgrservice.feature.post.model.Post;
import com.inlaco.crewmgrservice.feature.post.service.PostService;
import com.inlaco.crewmgrservice.utils.ConsoleUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Window;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/posts")
@PublicEndpoint
public record PostController(PostService postService) {

  @Operation(
      summary = "Create a new post with the given data using type field to identify the post type")
  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  public Post createPost(@RequestBody @Valid Post newPost) {
    return postService.createPost(newPost);
  }

  @Operation(summary = "Update a post with post Id")
  @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)
  @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json ")
  @ResponseStatus(HttpStatus.OK)
  public Post updatePost(@PathVariable("id") String id, @RequestBody JsonNode patch) {
    return postService.updatePost(id, patch);
  }

  @Operation(summary = "Delete a post with post Id")
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletePost(@PathVariable("id") String id) {
    postService.deletePost(id);
  }

  @GetMapping("/test")
  @Operation(
      summary = "Get all posts at the given window",
      description = "Get all posts at the given window")
  @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)
  public Window<?> getWindowPosts(@BearerToken String token) {
    ConsoleUtils.prettyPrint(token);
    return null;
  }

  @Operation(summary = "Get all posts at the given page")
  @PostMapping("/web")
  @ResponseStatus(HttpStatus.OK)
  public Page<?> getPagePosts(@PageableDefault(size = 10, page = 0) Pageable pageable) {
    return postService.getPagePosts(pageable);
  }

  @Operation(summary = "Get a post by its id")
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @PublicEndpoint
  public Post getPost(@PathVariable("id") String id) {
    return postService.getPost(id);
  }
}
