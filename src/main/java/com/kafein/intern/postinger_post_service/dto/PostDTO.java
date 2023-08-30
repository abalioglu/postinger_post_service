package com.kafein.intern.postinger_post_service.dto;

import lombok.Data;

@Data
public class PostDTO {
    private String id;
    private String username;
    private String description;
    private String imageUrl;
}
