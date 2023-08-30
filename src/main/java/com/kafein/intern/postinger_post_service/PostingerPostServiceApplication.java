package com.kafein.intern.postinger_post_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class PostingerPostServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(PostingerPostServiceApplication.class, args);
	}

}
