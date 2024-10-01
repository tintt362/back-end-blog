package com.trongtin.blog.identity.service;

import com.trongtin.blog.identity.dto.request.PostRequest;
import com.trongtin.blog.identity.dto.request.PostUpdateRequest;
import com.trongtin.blog.identity.dto.response.PostResponse;
import com.trongtin.blog.identity.entity.Post;
import com.trongtin.blog.identity.entity.User;
import com.trongtin.blog.identity.exception.AppException;
import com.trongtin.blog.identity.exception.ErrorCode;
import com.trongtin.blog.identity.mapper.PostMapper;
import com.trongtin.blog.identity.repository.PostRepository;
import com.trongtin.blog.identity.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private static final Logger log = LoggerFactory.getLogger(PostService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private KafkaProducer kafkaProducer;
    //create póst
    public PostResponse createPost(PostRequest request, MultipartFile file) throws IOException {
        // get user from token
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        // find user
        log.warn("request: " + request);
        log.warn("file: " + file);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        log.info("User: " + user.toString());
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .createdDate(Instant.now())
                .modifiedDate(Instant.now())
                .image(file.getBytes())
                .user(user)
                .build();

        postRepository.save(post);
        // Gửi sự kiện bài post mới tới Kafka topic 'new-post'
        kafkaProducer.sendNewPostEvent(post.getId());

        return postMapper.toPostResponse(post);
    }

    // Update post
    public PostResponse updatePost(String postId, PostUpdateRequest request, MultipartFile image) throws IOException {
        // Lấy user từ token
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();

        // Tìm bài viết theo ID
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new AppException(ErrorCode.POST_NOT_FOUND)
        );

        // Kiểm tra xem người dùng có phải chủ sở hữu của bài viết không
        if (!post.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        // Cập nhật thông tin bài viết
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setModifiedDate(Instant.now());

        // Nếu có ảnh mới thì cập nhật
        if (image != null && !image.isEmpty()) {
            post.setImage(image.getBytes());
        }

        // Lưu thay đổi vào cơ sở dữ liệu
        postRepository.save(post);

        return postMapper.toPostResponse(post);
    }

     @PreAuthorize("hasRole('ADMIN')")
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(
                (post) -> postMapper.toPostResponse(post)
        ).toList();
    }

    public PostResponse getPostByTitle(String title) {
        Post post = postRepository.findByTitle(title).orElseThrow(
                () -> new AppException(ErrorCode.POST_TITLE_NOT_EXISTED)
        );
        return postMapper.toPostResponse(post);
    }

    public void deletePost(String postId) {

        postRepository.deleteById(postId);
    }

    public void likePost(String postId) {
        // Lấy user từ token (hoặc session)
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new AppException(ErrorCode.POST_NOT_FOUND)
        );

        // Thêm bài viết vào danh sách yêu thích của người dùng
        user.getLikedPosts().add(post);
        post.getLikedByUsers().add(user);
        post.setLikeCount((long) post.getLikedByUsers().size());

        postRepository.save(post);
    }

    // Xóa lượt thích
    public void unlikePost(String postId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new AppException(ErrorCode.POST_NOT_FOUND)
        );

        user.getLikedPosts().remove(post);
        post.getLikedByUsers().remove(user);

        post.setLikeCount((long) post.getLikedByUsers().size());
        postRepository.save(post);
    }
}
