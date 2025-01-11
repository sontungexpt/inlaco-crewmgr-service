package com.inlaco.crewmgrservice.feature.post.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.annotation.CurrentUser;
import com.inlaco.crewmgrservice.annotation.PageableQueryParams;
import com.inlaco.crewmgrservice.annotation.PublicEndpoint;
import com.inlaco.crewmgrservice.config.OpenApiConfig;
import com.inlaco.crewmgrservice.endpoint.APIEndpointMap;
import com.inlaco.crewmgrservice.endpoint.APIEndpointName;
import com.inlaco.crewmgrservice.feature.post.model.Post;
import com.inlaco.crewmgrservice.feature.post.service.PostService;
import com.inlaco.crewmgrservice.feature.user.model.User;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@PublicEndpoint
@RequiredArgsConstructor
@Tag(name = "Post", description = "A collection endpoints to work with posts")
public class PostController {

  private final PostService postService;

  @Operation(
      summary = "Create a new post with the given data using type field to identify the post type",
      description =
          """
Create a new post with the given data using type field to identify the post type.

**Usecase**:

- UC_admin-dang-thong-tin-tuyen-dung
""",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  @APIEndpointMap(
      name = APIEndpointName.POST_CREATE,
      displayName = "Create a new post",
      description =
          "Create a new post with the given data using type field to identify the post type")
  @PublicEndpoint(profiles = "dev")
  public Post createPost(@CurrentUser User user, @RequestBody @Valid Post newPost) {
    return postService.createPost(newPost, user);
  }

  @Operation(
      summary = "Update a post with post Id",
      description = "Update a post with post Id",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
  @ResponseStatus(HttpStatus.OK)
  @APIEndpointMap(
      name = APIEndpointName.POST_UPDATE,
      displayName = "Update a post",
      description = "Update a post with post Id")
  public Post updatePost(
      @CurrentUser User user, @PathVariable("id") String id, @RequestBody JsonNode patch) {
    return postService.updatePost(id, patch, user);
  }

  @Operation(
      summary = "Delete a post with post Id",
      description = "Delete a post with post Id",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @APIEndpointMap(
      name = APIEndpointName.POST_DELETE,
      displayName = "Delete a post",
      description = "Delete a post with post Id")
  public void deletePost(@CurrentUser User user, @PathVariable("id") String id) {
    postService.deletePost(id, user);
  }

  // @GetMapping("/test")
  // @Operation(
  //     summary = "Get all posts at the given window",
  //     description = "Get all posts at the given window",
  //     security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  // public Window<?> getWindowPosts(@BearerToken String token) {
  //   ConsoleUtils.prettyPrint(token);
  //   return null;
  // }

  @Operation(
      summary = "Retrieve all posts for a given page",
      description =
          """
This API retrieves a list of posts from the server based on the specified page number and size.

**Use cases:**
- UC_general-user-xem-thong-tin-tuyen-dung.

**Notes:**
- Pagination is required.
- Sorting is optional but can be applied.

""")
  @GetMapping("/web")
  @ResponseStatus(HttpStatus.OK)
  @PageableQueryParams
  @APIEndpointMap(
      name = APIEndpointName.POST_READ,
      displayName = "Retrieve all posts for a given page",
      description =
          "This API retrieves a list of posts from the server based on the specified page number"
              + " and size.")
  public Page<?> getPagePosts(@PageableDefault(size = 10, page = 0) Pageable pageable) {
    return postService.getPagePosts(pageable);
  }

  @Operation(
      summary = "Retrieve a post from the server by id",
      description =
          """
This API retrieves a post from the server based on its id.

**Use cases:**
- UC_general-user-xem-thong-tin-tuyen-dung.

""")
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @PublicEndpoint
  @APIEndpointMap(
      name = APIEndpointName.POST_READ,
      displayName = "Retrieve a post from the server by id",
      description = "This API retrieves a post from the server based on its id.")
  public Post getPost(@PathVariable("id") String id) {
    return postService.getPost(id);
  }
}
