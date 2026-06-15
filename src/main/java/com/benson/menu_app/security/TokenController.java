package com.benson.menu_app.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TokenController {

    private final JwtUtil jwtUtil;

    // Hardcoded credentials for simplicity
    private final String HARDCODED_CLIENT_ID = "my-client-id";
    private final String HARDCODED_CLIENT_SECRET = "my-super-secret";

    public TokenController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/token")
    public ResponseEntity<?> createToken(@RequestBody TokenRequest request) {
        if (HARDCODED_CLIENT_ID.equals(request.clientId()) &&
                HARDCODED_CLIENT_SECRET.equals(request.clientSecret())) {

            String token = jwtUtil.generateToken(request.clientId());
            return ResponseEntity.ok(Map.of("access_token", token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Client Credentials");

    }

}