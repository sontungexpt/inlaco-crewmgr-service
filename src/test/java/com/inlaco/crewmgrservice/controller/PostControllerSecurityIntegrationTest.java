// package com.inlaco.crewmgrservice.controller;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import com.inlaco.crewmgrservice.feature.post.application.port.in.PostUseCase;
// import com.inlaco.crewmgrservice.feature.post.domain.model.NewsPost;
// import com.inlaco.crewmgrservice.feature.post.presentation.dto.NewsPostDTO;
// import com.inlaco.crewmgrservice.feature.post.presentation.dto.PostDTO;
// import com.inlaco.crewmgrservice.feature.post.presentation.mapper.PostMapper;
// import java.util.List;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Nested;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.web.servlet.MockMvc;

// @SpringBootTest
// @AutoConfigureMockMvc
// class PostControllerSecurityIntegrationTest {

//   @Autowired private MockMvc mockMvc;

//   @MockitoBean private PostUseCase postUseCase;

//   @MockitoBean private PostMapper postMapper;

//   private PostDTO mockDto() {
//     return new NewsPostDTO(); // minimal stub
//   }

//   /* ==========================================================
//    * PUBLIC (class-level @PublicEndpoint)
//    * ========================================================== */

//   @Nested
//   class PublicEndpoints {

//     @Test
//     @DisplayName("GET /{id} should allow anonymous")
//     void getPost_shouldAllowAnonymous() throws Exception {
//       when(postUseCase.getPost(any())).thenReturn(new NewsPost());
//       when(postMapper.toPostDTO(any())).thenReturn(mockDto());

//       mockMvc.perform(get("/api/v1/posts/1")).andExpect(status().isOk());
//     }

//     @Test
//     @DisplayName("GET / should allow anonymous")
//     void getAllPosts_shouldAllowAnonymous() throws Exception {
//       when(postUseCase.getPosts(any(), any())).thenReturn(new PageImpl<>(List.of(new
// NewsPost())));
//       when(postMapper.toPostDTO(any())).thenReturn(mockDto());
//       mockMvc.perform(get("/api/v1/posts")).andExpect(status().isOk());
//     }
//   }

//   /* ==========================================================
//    * DEV PROFILE PUBLIC (method-level override)
//    * ========================================================== */

//   @Nested
//   @ActiveProfiles("dev")
//   class DevProfilePublicEndpoint {

//     @Test
//     @DisplayName("POST should allow anonymous in dev profile")
//     void createPost_shouldAllowAnonymous_inDev() throws Exception {

//       when(postMapper.toPost(any())).thenReturn(new NewsPost());
//       when(postUseCase.createPost(any(), any())).thenReturn(new NewsPost());
//       when(postMapper.toPostDTO(any())).thenReturn(mockDto());

//       mockMvc
//           .perform(
//               post("/api/v1/posts")
//                   .contentType(MediaType.APPLICATION_JSON)
//                   .content(
// """
// {
//   "title": "Test title"
// }
// """))
//           .andExpect(status().isCreated());
//     }
//   }

//   /* ==========================================================
//    * PROTECTED ENDPOINTS
//    * ========================================================== */

//   // @Nested
//   // class ProtectedEndpoints {

//   //   @Test
//   //   @DisplayName("PATCH should reject anonymous")
//   //   void updatePost_shouldRejectAnonymous() throws Exception {
//   //     mockMvc
//   //         .perform(
//   //
// patch("/api/v1/posts/1").contentType("application/merge-patch+json").content("{}"))
//   //         .andExpect(status().isUnauthorized());
//   //   }

//   //   @Test
//   //   @DisplayName("PATCH should allow JWT")
//   //   void updatePost_shouldAllowJwt() throws Exception {

//   //     when(postMapper.toPostUpdateCommand(any())).thenReturn(new PostUpdateCommand());
//   //     when(postUseCase.updatePost(any(), any(), any())).thenReturn(new NewsPost());
//   //     when(postMapper.toPostDTO(any())).thenReturn(mockDto());

//   //     mockMvc
//   //         .perform(
//   //             patch("/api/v1/posts/1")
//   //                 .with(jwt())
//   //                 .contentType("application/merge-patch+json")
//   //                 .content("{}"))
//   //         .andExpect(status().isOk());
//   //   }

//   //   @Test
//   //   @DisplayName("DELETE should reject anonymous")
//   //   void deletePost_shouldRejectAnonymous() throws Exception {
//   //     mockMvc.perform(delete("/api/v1/posts/1")).andExpect(status().isUnauthorized());
//   //   }

//   //   @Test
//   //   @DisplayName("DELETE should allow JWT")
//   //   void deletePost_shouldAllowJwt() throws Exception {
//   //
// mockMvc.perform(delete("/api/v1/posts/1").with(jwt())).andExpect(status().isNoContent());
//   //   }
//   // }
// }
