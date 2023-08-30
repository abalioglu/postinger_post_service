package com.kafein.intern.postinger_post_service.service;

import com.kafein.intern.postinger_post_service.dto.PostDTO;
import com.kafein.intern.postinger_post_service.model.Post;
import com.kafein.intern.postinger_post_service.repository.PostRepository;
import feign.Feign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    public String getToken(HttpServletRequest request){
        String jwt = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWT_TOKEN")) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }return jwt;
    }
    public Post createOrUpdatePost(Post post) {
        if (post.getId() == null) {
            post.setId(UUID.randomUUID().toString());
        }
        return postRepository.save(post);
    }
    public void deletePost(String postId) {
        postRepository.deleteById(postId);
    }
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    public Post getPostById(String postId) {
        return postRepository.findById(postId).orElse(null);
    }
    public List<Post> getAllByUserId(Long userId) {
        return postRepository.findAllByUserId(userId);
    }
    public Long getUserIdByPostId(String postId){
        Post post = getPostById(postId);
        return post.getUserId();
    }

}

