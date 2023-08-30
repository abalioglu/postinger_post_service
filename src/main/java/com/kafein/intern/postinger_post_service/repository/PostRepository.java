package com.kafein.intern.postinger_post_service.repository;

import com.kafein.intern.postinger_post_service.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findAllByUserId(Long userId);
}
