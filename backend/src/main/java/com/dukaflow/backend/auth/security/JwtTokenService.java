package com.dukaflow.backend.auth.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;
    private final Duration accessTokenTtl;
    private final Duration refreshTokenTtl;

    public JwtTokenService(
            JwtEncoder jwtEncoder,
            @Value("${app.security.jwt.access-token-ttl}") Duration accessTokenTtl,
            @Value("${app.security.jwt.refresh-token-ttl}") Duration refreshTokenTtl) {
        this.jwtEncoder = jwtEncoder;
        this.accessTokenTtl = accessTokenTtl;
        this.refreshTokenTtl = refreshTokenTtl;
    }

    public String generateAccessToken(DukaFlowUserDetails userDetails) {
        Instant now = Instant.now();

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetails.getUserId());
        claims.put("businessId", userDetails.getBusinessId());
        claims.put("fullName", userDetails.getFullName());
        claims.put("email", userDetails.getEmail());
        claims.put("role", userDetails.getRoleName());
        claims.put("branchIds", userDetails.getBranchIds());
        claims.put("tokenType", "access");

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("dukaflow-ai")
                .issuedAt(now)
                .expiresAt(now.plus(accessTokenTtl))
                .subject(userDetails.getUsername())
                .claims(existingClaims -> existingClaims.putAll(claims))
                .build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(claimsSet))
                .getTokenValue();
    }

    public String generateRefreshToken(DukaFlowUserDetails userDetails) {
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("dukaflow-ai")
                .issuedAt(now)
                .expiresAt(now.plus(refreshTokenTtl))
                .subject(userDetails.getUsername())
                .claim("userId", userDetails.getUserId())
                .claim("tokenType", "refresh")
                .build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(claimsSet))
                .getTokenValue();
    }
}
