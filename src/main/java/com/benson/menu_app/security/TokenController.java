package com.benson.menu_app.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TokenController {

    private final JwtUtil jwtUtil;

    @Value("${app.security.client-id}")
    private String clientId;

    @Value("${app.security.client-secret}")
    private String clientSecret;

    public TokenController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/token")
    public ResponseEntity<?> createToken(@RequestBody TokenRequest request) {
        if (clientId.equals(request.clientId()) &&
                clientSecret.equals(request.clientSecret())) {

            String token = jwtUtil.generateToken(request.clientId());
            return ResponseEntity.ok(Map.of("access_token", token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Client Credentials");

    }

}