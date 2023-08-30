package com.kafein.intern.postinger_post_service.controller;

import com.kafein.intern.postinger_post_service.dto.PostDTO;
import com.kafein.intern.postinger_post_service.jwt.JwtUtil;
import com.kafein.intern.postinger_post_service.model.Post;
import com.kafein.intern.postinger_post_service.service.IdentityClientService;
import com.kafein.intern.postinger_post_service.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final JwtUtil jwtUtil;
    private final IdentityClientService identityClientService;

    @PostMapping("/create-post") //Identity Service'te çağırılan
    public ResponseEntity<Post> createPost(@RequestBody Post post, HttpServletRequest request) {
        String jwt = postService.getToken(request);
        Long userId = jwtUtil.extractIdClaim(jwt);
        if(post.getId() == null && post.getUserId() == null){
            post.setUserId((userId));
            Post savedPost = postService.createOrUpdatePost(post);
            return ResponseEntity.ok(savedPost);
        }
        else throw new RuntimeException("ID and UserID must be null");
    }
    @PostMapping("/update-post") //Identity Servicete çağırdığım
    public ResponseEntity<Post> updatePost(@RequestBody Post post, HttpServletRequest request) {
        String jwt = postService.getToken(request);
        Long userId = jwtUtil.extractIdClaim(jwt);
        if (post.getId() != null && post.getUserId() == null) {
            if(Objects.equals(postService.getUserIdByPostId(post.getId()), userId))  {
                post.setUserId(userId);
                Post updatedPost = postService.createOrUpdatePost(post);
                return ResponseEntity.ok(updatedPost);
            }else throw new RuntimeException("User doesn't have a post with this ID");
        }else throw new RuntimeException("Post ID must not be null and Username must be null");
    }

    @PostMapping("/create-post-dto") // yapabiliyor muyum görmek için
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO, HttpServletRequest request) {
        String jwt = postService.getToken(request);
        Long userId = jwtUtil.extractIdClaim(jwt);
        String username = identityClientService.getUsername(userId);
        if(postDTO.getId() == null && postDTO.getUsername() == null){
            Post savedPost = new Post();
            savedPost.setDescription(postDTO.getDescription());
            savedPost.setImageUrl(postDTO.getImageUrl());
            savedPost.setUserId(userId);
            Post post = postService.createOrUpdatePost(savedPost);
            PostDTO returnedDTO = new PostDTO();
            returnedDTO.setDescription(post.getDescription());
            returnedDTO.setImageUrl(post.getImageUrl());
            returnedDTO.setId(post.getId());
            returnedDTO.setUsername(username);
            return ResponseEntity.ok(returnedDTO);
        }
        else throw new RuntimeException("ID and Username must be null");
    }

    @PutMapping("/update-post-dto")
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO,  HttpServletRequest request) {
        String jwt = postService.getToken(request);
        Long userId = jwtUtil.extractIdClaim(jwt);
        String username = identityClientService.getUsername(userId);
        if(postDTO.getId() != null && postDTO.getUsername() == null) {
            if (Objects.equals(postService.getUserIdByPostId(postDTO.getId()), userId)) {
                Post post = new Post();
                post.setImageUrl(postDTO.getImageUrl());
                post.setDescription(postDTO.getDescription());
                post.setUserId(postService.getUserIdByPostId(postDTO.getId()));
                post.setId(postDTO.getId());
                Post updatedPost = postService.createOrUpdatePost(post);
                PostDTO returnedDTO = new PostDTO();
                returnedDTO.setDescription(updatedPost.getDescription());
                returnedDTO.setImageUrl(updatedPost.getImageUrl());
                returnedDTO.setId(updatedPost.getId());
                returnedDTO.setUsername(username);
                return ResponseEntity.ok(returnedDTO);
            } else throw new RuntimeException("User doesn't have a post with this ID");
        } else throw new RuntimeException(("Post ID must not be null and Username must be null"));
    }

    @DeleteMapping("/delete-post")
    public ResponseEntity<String> deletePost(@RequestParam(name = "postId") String id, HttpServletRequest request) {
        String jwt = postService.getToken(request);
        Long userId = jwtUtil.extractIdClaim(jwt);
        if (Objects.equals(postService.getUserIdByPostId(id), userId)) {
            postService.deletePost(id);
            return ResponseEntity.ok("Post deleted successfully");
        } else throw new RuntimeException("User doesn't have a post with this ID");
    }

    @GetMapping("/get-all-posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/get-post-by-id")
    public ResponseEntity<Post> getPostById(@RequestParam(name = "postId") String id) {
        Post post = postService.getPostById(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping(value = "/user-posts")
    public ResponseEntity<List<Post>> getPostsByUserId(@RequestParam(value = "userId") Long userId){
        return new ResponseEntity<>(postService.getAllByUserId(userId), HttpStatus.OK);
    }

    @GetMapping(value = "/get-userId-by-postId")
    public ResponseEntity<Long> getUserIdByPostId(@RequestParam(name = "postId") String postId) {
        Long userId = postService.getUserIdByPostId(postId);
        if(userId != null)
            return ResponseEntity.ok(userId);
        else
            throw new RuntimeException("There is no Post with this Id");
    }
}

