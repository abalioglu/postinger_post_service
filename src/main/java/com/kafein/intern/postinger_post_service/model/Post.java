package com.kafein.intern.postinger_post_service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "posts")
public class Post {
    @Id
    private String id;
    private Long userId;
    private String description;
    private String imageUrl;


}
