package com.example.fastapi.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private Key secretKey;
    private final long expirationMillis = 1000 * 60 * 60 * 24; // 24 ساعت

    @PostConstruct
    public void init() {
        // ساخت کلید مخفی برای امضای JWT
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    /**
     * تولید توکن JWT
     *
     * @param username  نام کاربری
     * @param storeName نام فروشگاه
     * @param role      نقش کاربر
     * @return توکن JWT به صورت رشته
     */
    public String generateToken(String username,
                                String storeName,
                                String role,
                                boolean inventoryEnabled,
                                boolean customerEnabled) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(username)
                .claim("storeName", storeName)
                .claim("role", role)
                .claim("inventoryEnabled", inventoryEnabled)
                .claim("customerEnabled", customerEnabled)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * اعتبارسنجی توکن JWT
     *
     * @param token توکن JWT
     * @return true اگر توکن معتبر است، false در غیر این صورت
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // توکن نامعتبر یا منقضی شده است
            return false;
        }
    }

    /**
     * استخراج نام کاربری از توکن JWT
     *
     * @param token توکن JWT
     * @return نام کاربری موجود در توکن
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * استخراج نام فروشگاه از توکن JWT
     *
     * @param token توکن JWT
     * @return نام فروشگاه موجود در توکن
     */
    public String getStoreNameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("storeName", String.class);
    }

    /**
     * استخراج نقش کاربر از توکن JWT
     *
     * @param token توکن JWT
     * @return نقش کاربر موجود در توکن
     */
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }
}
