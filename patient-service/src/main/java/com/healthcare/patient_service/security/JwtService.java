package com.healthcare.patient_service.security;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;


// PURPOSE: This class does 3 things:
// 1. generateToken()  — after login, create a JWT with username + role inside
// 2. extractUsername() — from an incoming JWT, read WHO this person is
// 3. isTokenValid()    — check: is the signature correct? is it expired?
//
// ANALOGY: This is the "stamp machine" at the hospital reception.
//          It CREATES official stamps (tokens) and VERIFIES them.

@Service
public class JwtService {
    // WHY @Value? We don't hardcode the secret key in Java code.
    // It comes from application.yaml. In production, it comes from
    // environment variables or a vault (like AWS Secrets Manager).
    // If the key is in code and you push to GitHub — it's LEAKED.
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private long expirationMs;
    // ──── CREATE A TOKEN ────
    // Called after successful login.
    // Puts username and role INSIDE the JWT payload.
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)              // "sub" claim — WHO is this token for
                .claim("role", role)             // custom claim — WHAT can they do
                .issuedAt(new Date())            // "iat" claim — WHEN was it created
                .expiration(new Date(             // "exp" claim — WHEN does it expire
                        System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())        // SIGN with our secret key
                .compact();                       // Build the final string: header.payload.signature
    }

    // ──── READ USERNAME FROM TOKEN ────
    // Called by JwtAuthenticationFilter on every request.
    // Opens the JWT and reads the "sub" (subject) claim.
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // ──── VALIDATE TOKEN ────
    // Two checks:
    // 1. Is the signature valid? (was it tampered with?)
    //    → If tampered, Jwts.parser() throws SignatureException
    // 2. Is it expired?
    //    → If expired, throws ExpiredJwtException
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);  // this throws if signature is bad or expired
            return true;
        } catch (Exception e) {
            return false;              // any problem → token is invalid
        }
    }

    // ──── INTERNAL: Parse ALL claims from the token ────
    // This is where the SIGNATURE VERIFICATION happens.
    // Jwts.parser() recalculates the signature using OUR secret key
    // and compares it with the signature IN the token.
    // If they don't match → SignatureException (tampered!)
    // If token is expired → ExpiredJwtException
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())       // use our secret key to verify
                .build()
                .parseSignedClaims(token)           // parse + verify signature
                .getPayload();                      // return the claims (payload)
    }

    // ──── INTERNAL: Convert String secret to a cryptographic key ────
    // WHY? You can't sign a JWT with a plain String.
    // The HMAC-SHA256 algorithm needs a SecretKey object.
    // Keys.hmacShaKeyFor() converts your secret string into the right format.
    // IMPORTANT: The secret must be at least 256 bits (32 characters) for HS256.
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }



}
