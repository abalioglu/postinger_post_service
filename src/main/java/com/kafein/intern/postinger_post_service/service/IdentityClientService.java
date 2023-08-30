package com.kafein.intern.postinger_post_service.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "postinger-identity-service", url = "http://localhost:8090")
public interface IdentityClientService {
    @GetMapping("/user/get-username")
    public String getUsername(@RequestParam(name = "id") Long userId);

}
