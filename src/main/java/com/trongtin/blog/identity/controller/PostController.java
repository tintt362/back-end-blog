package com.trongtin.blog.identity.controller;


import com.trongtin.blog.identity.dto.request.PostRequest;
import com.trongtin.blog.identity.dto.request.PostUpdateRequest;
import com.trongtin.blog.identity.dto.response.ApiResponse;
import com.trongtin.blog.identity.dto.response.PostResponse;
import com.trongtin.blog.identity.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ApiResponse<PostResponse> createPost(@ModelAttribute PostRequest request,
                                                 @RequestParam("file") MultipartFile file
    ) throws IOException {

        return ApiResponse.<PostResponse>builder()
                .result(postService.createPost(request, file)).build();
    }

    @PutMapping("/updates/{postId}")
    public ApiResponse<PostResponse> updatePost(@ModelAttribute PostUpdateRequest request,
                                                @RequestParam("file") MultipartFile file,
                                                @PathVariable("postId") String postId) throws IOException {


        return ApiResponse.<PostResponse>builder()
                .result(postService.updatePost(postId, request, file)).build();
    }

    @GetMapping()
    public ApiResponse<List<PostResponse>> getAllPosts() {
        return ApiResponse.<List<PostResponse>>builder()
                .result(postService.getAllPosts())
                .build();
    }

    @GetMapping("/{title}")
    public ApiResponse<PostResponse> getPostById(@PathVariable String title) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.getPostByTitle(title))
                .build();
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable String postId) {
        postService.deletePost(postId);
        return ApiResponse.<Void>builder()

                .build();
    }
    // API để xóa lượt thích
    @DeleteMapping("/{postId}/like")
    public ResponseEntity<ApiResponse<String>> unlikePost(@PathVariable String postId) {
        postService.unlikePost(postId);
        return ResponseEntity.ok(ApiResponse.<String>builder().result("Unliked").build());
    }
    @PostMapping("/{postId}/like")
    public ResponseEntity<ApiResponse<String>> likePost(@PathVariable String postId) {
        postService.likePost(postId);
        return ResponseEntity.ok(ApiResponse.<String>builder().result("Liked").build());
    }}


