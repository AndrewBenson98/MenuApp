package com.benson.menu_app.security;

import java.util.Date;

public record TokenResponse(String token, Date issuedAt, Date expiresAt) {
}
