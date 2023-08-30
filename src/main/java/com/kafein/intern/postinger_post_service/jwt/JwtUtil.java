package com.kafein.intern.postinger_post_service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private String secretKey = "XOMmbTa4keacmII06k7toYAJDnRpcgl3+v89wqPci9y1TKbNmO76U7ONYDhCuYio+Q/g2IMAX2eY4MQ0g/I3aQ==";
    public Long extractIdClaim(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        Long id = claims.get("id", Long.class);
        return id;
    }

}
