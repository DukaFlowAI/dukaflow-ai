package com.dukaflow.backend.auth.service;

import com.dukaflow.backend.auth.dto.LoginRequest;
import com.dukaflow.backend.auth.dto.LoginResponse;
import com.dukaflow.backend.auth.security.DukaFlowUserDetails;
import com.dukaflow.backend.auth.security.JwtTokenService;
import com.dukaflow.backend.user.entity.User;
import com.dukaflow.backend.user.entity.UserBranch;
import com.dukaflow.backend.user.repository.UserBranchRepository;
import com.dukaflow.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dukaflow.backend.auth.dto.CurrentUserResponse;
import com.dukaflow.backend.auth.dto.RefreshTokenRequest;
import com.dukaflow.backend.auth.dto.RefreshTokenResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import com.dukaflow.backend.auth.exception.InvalidRefreshTokenException;
import com.dukaflow.backend.auth.exception.AuthenticatedUserNotFoundException;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final UserBranchRepository userBranchRepository;
    private final JwtDecoder jwtDecoder;
    private final Duration accessTokenTtl;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtTokenService jwtTokenService,
            UserRepository userRepository,
            UserBranchRepository userBranchRepository,
            JwtDecoder jwtDecoder,
            @Value("${app.security.jwt.access-token-ttl}") Duration accessTokenTtl) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
        this.userBranchRepository = userBranchRepository;
        this.jwtDecoder = jwtDecoder;
        this.accessTokenTtl = accessTokenTtl;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.identifier(),
                        request.password()));

        DukaFlowUserDetails userDetails = (DukaFlowUserDetails) authentication.getPrincipal();

        User user = userRepository
                .findByEmailIgnoreCase(userDetails.getEmail())
                .orElseThrow();

        List<LoginResponse.Branch> branches = userBranchRepository
                .findByUser_Id(user.getId())
                .stream()
                .map(UserBranch::getBranch)
                .map(branch -> new LoginResponse.Branch(
                        branch.getId(),
                        branch.getBranchCode(),
                        branch.getName()))
                .toList();

        LoginResponse.User responseUser = new LoginResponse.User(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                new LoginResponse.Role(
                        user.getRole().getId(),
                        user.getRole().getName()),
                new LoginResponse.Business(
                        user.getBusiness().getId(),
                        user.getBusiness().getName()),
                branches);

        String accessToken = jwtTokenService.generateAccessToken(userDetails);
        String refreshToken = jwtTokenService.generateRefreshToken(userDetails);

        LoginResponse.Data data = new LoginResponse.Data(
                accessToken,
                refreshToken,
                "Bearer",
                accessTokenTtl.toSeconds(),
                responseUser);

        return new LoginResponse(
                true,
                "Login successful",
                data,
                Instant.now());
    }

    @Transactional(readOnly = true)
    public CurrentUserResponse getCurrentUser(Long userId) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new AuthenticatedUserNotFoundException(
                        "Authenticated user was not found."));

        List<LoginResponse.Branch> branches = userBranchRepository
                .findByUser_Id(user.getId())
                .stream()
                .map(UserBranch::getBranch)
                .map(branch -> new LoginResponse.Branch(
                        branch.getId(),
                        branch.getBranchCode(),
                        branch.getName()))
                .toList();

        LoginResponse.User responseUser = new LoginResponse.User(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                new LoginResponse.Role(
                        user.getRole().getId(),
                        user.getRole().getName()),
                new LoginResponse.Business(
                        user.getBusiness().getId(),
                        user.getBusiness().getName()),
                branches);

        return new CurrentUserResponse(
                true,
                "Current user retrieved successfully",
                responseUser,
                Instant.now());
    }

    @Transactional(readOnly = true)
    public RefreshTokenResponse refresh(RefreshTokenRequest request) {

        Jwt jwt = jwtDecoder.decode(request.refreshToken());

        String tokenType = jwt.getClaimAsString("tokenType");

        if (!"refresh".equals(tokenType)) {
            throw new InvalidRefreshTokenException("Invalid refresh token.");
        }

        Long userId = jwt.getClaim("userId");

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new AuthenticatedUserNotFoundException(
                        "Authenticated user was not found."));

        if (!user.isActive()) {
            throw new InvalidRefreshTokenException(
                    "User account is inactive.");
        }

        DukaFlowUserDetails userDetails = new DukaFlowUserDetails(
                user.getId(),
                user.getBusiness().getId(),
                user.getFullName(),
                user.getEmail(),
                user.getUsername(),
                user.getPasswordHash(),
                user.getRole().getName(),
                userBranchRepository
                        .findByUser_Id(user.getId())
                        .stream()
                        .map(userBranch -> userBranch.getBranch().getId())
                        .collect(java.util.stream.Collectors.toSet()),
                user.isActive());

        String accessToken = jwtTokenService.generateAccessToken(userDetails);

        String refreshToken = jwtTokenService.generateRefreshToken(userDetails);

        RefreshTokenResponse.Data data = new RefreshTokenResponse.Data(
                accessToken,
                refreshToken,
                "Bearer",
                accessTokenTtl.toSeconds());

        return new RefreshTokenResponse(
                true,
                "Token refreshed successfully",
                data,
                Instant.now());
    }
}
