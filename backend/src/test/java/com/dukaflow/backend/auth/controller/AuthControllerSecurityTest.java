package com.dukaflow.backend.auth.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.core.Authentication;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertTrue;
import com.dukaflow.backend.branch.entity.Branch;
import com.dukaflow.backend.branch.repository.BranchRepository;
import com.dukaflow.backend.business.entity.Business;
import com.dukaflow.backend.business.repository.BusinessRepository;
import com.dukaflow.backend.role.entity.Role;
import com.dukaflow.backend.role.repository.RoleRepository;
import com.dukaflow.backend.user.entity.User;
import com.dukaflow.backend.user.entity.UserBranch;
import com.dukaflow.backend.user.repository.UserBranchRepository;
import com.dukaflow.backend.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import com.dukaflow.backend.auth.security.DukaFlowUserDetails;
import com.dukaflow.backend.auth.security.JwtTokenService;
import org.springframework.http.HttpHeaders;

import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtAuthenticationConverter jwtAuthenticationConverter;
    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBranchRepository userBranchRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Test
    void meWithoutTokenReturnsUnauthorized() throws Exception {

        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("AUTHENTICATION_REQUIRED"));
    }

    @Test
    void refreshWithInvalidTokenReturnsUnauthorized() throws Exception {

        mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "refreshToken": "not-a-valid-jwt"
                        }
                        """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("AUTHENTICATION_REQUIRED"));
    }

    @Test
    void refreshWithBlankTokenReturnsValidationError() throws Exception {

        mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "refreshToken": ""
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errors[0].field").value("refreshToken"));
    }

    @Test
    void loginWithBlankFieldsReturnsValidationError() throws Exception {

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "identifier": "",
                          "password": ""
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void loginWithInvalidCredentialsReturnsUnauthorized() throws Exception {

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "identifier": "nonexistent-user@dukaflow.test",
                          "password": "wrong-password"
                        }
                        """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("INVALID_CREDENTIALS"));
    }

    @Test
    void jwtRoleClaimIsConvertedToSpringRoleAuthority() {

        Jwt jwt = new Jwt(
                "test-token",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                java.util.Map.of("alg", "HS256"),
                java.util.Map.of(
                        "sub", "owner@dukaflow.test",
                        "role", "BUSINESS_OWNER"));

        Authentication authentication = jwtAuthenticationConverter.convert(jwt);

        assertTrue(
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority -> authority.getAuthority()
                                .equals("ROLE_BUSINESS_OWNER")));
    }

    @Test
    @Transactional
    void loginWithValidCredentialsReturnsTokensAndUserContext() throws Exception {

        Business business = businessRepository.save(
                new Business(
                        "DukaFlow Test Business",
                        "AUTH-TEST-BIZ",
                        null,
                        "business@dukaflow.test",
                        null,
                        "KES"));

        Role role = roleRepository.save(
                new Role(
                        business,
                        "BUSINESS_OWNER",
                        "Test business owner",
                        true));

        Branch branch = branchRepository.save(
                new Branch(
                        business,
                        "MAIN",
                        "Main Branch",
                        null,
                        null,
                        null));

        User user = userRepository.save(
                new User(
                        business,
                        role,
                        "DukaFlow Test Owner",
                        "auth-owner@dukaflow.test",
                        "authowner",
                        passwordEncoder.encode("TestPassword123!"),
                        null));

        userBranchRepository.save(
                new UserBranch(user, branch));

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "identifier": "auth-owner@dukaflow.test",
                          "password": "TestPassword123!"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.data.user.email")
                        .value("auth-owner@dukaflow.test"))
                .andExpect(jsonPath("$.data.user.role.name")
                        .value("BUSINESS_OWNER"))
                .andExpect(jsonPath("$.data.user.business.name")
                        .value("DukaFlow Test Business"))
                .andExpect(jsonPath("$.data.user.branches[0].code")
                        .value("MAIN"));
    }

    @Test
    @Transactional
    void meWithValidAccessTokenReturnsCurrentUser() throws Exception {

        Business business = businessRepository.save(
                new Business(
                        "DukaFlow Me Test Business",
                        "ME-TEST-BIZ",
                        null,
                        "me-business@dukaflow.test",
                        null,
                        "KES"));

        Role role = roleRepository.save(
                new Role(
                        business,
                        "BUSINESS_OWNER",
                        "Test business owner",
                        true));

        Branch branch = branchRepository.save(
                new Branch(
                        business,
                        "ME-MAIN",
                        "Me Main Branch",
                        null,
                        null,
                        null));

        User user = userRepository.save(
                new User(
                        business,
                        role,
                        "Current User Test",
                        "current-user@dukaflow.test",
                        "currentuser",
                        passwordEncoder.encode("TestPassword123!"),
                        null));

        userBranchRepository.save(
                new UserBranch(user, branch));

        DukaFlowUserDetails userDetails = new DukaFlowUserDetails(
                user.getId(),
                business.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getUsername(),
                user.getPasswordHash(),
                role.getName(),
                Set.of(branch.getId()),
                user.isActive());

        String accessToken = jwtTokenService.generateAccessToken(userDetails);

        mockMvc.perform(get("/api/v1/auth/me")
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(user.getId()))
                .andExpect(jsonPath("$.data.email")
                        .value("current-user@dukaflow.test"))
                .andExpect(jsonPath("$.data.role.name")
                        .value("BUSINESS_OWNER"))
                .andExpect(jsonPath("$.data.business.name")
                        .value("DukaFlow Me Test Business"))
                .andExpect(jsonPath("$.data.branches[0].code")
                        .value("ME-MAIN"));
    }

    @Test
    @Transactional
    void refreshWithValidRefreshTokenReturnsNewTokens() throws Exception {

        Business business = businessRepository.save(
                new Business(
                        "DukaFlow Refresh Test Business",
                        "REFRESH-TEST-BIZ",
                        null,
                        "refresh-business@dukaflow.test",
                        null,
                        "KES"));

        Role role = roleRepository.save(
                new Role(
                        business,
                        "BUSINESS_OWNER",
                        "Test business owner",
                        true));

        Branch branch = branchRepository.save(
                new Branch(
                        business,
                        "REFRESH-MAIN",
                        "Refresh Main Branch",
                        null,
                        null,
                        null));

        User user = userRepository.save(
                new User(
                        business,
                        role,
                        "Refresh User Test",
                        "refresh-user@dukaflow.test",
                        "refreshuser",
                        passwordEncoder.encode("TestPassword123!"),
                        null));

        userBranchRepository.save(
                new UserBranch(user, branch));

        DukaFlowUserDetails userDetails = new DukaFlowUserDetails(
                user.getId(),
                business.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getUsername(),
                user.getPasswordHash(),
                role.getName(),
                Set.of(branch.getId()),
                user.isActive());

        String refreshToken = jwtTokenService.generateRefreshToken(userDetails);

        mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "refreshToken": "%s"
                        }
                        """.formatted(refreshToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"));
    }

    @Test
    @Transactional
    void refreshWithAccessTokenReturnsUnauthorized() throws Exception {

        Business business = businessRepository.save(
                new Business(
                        "DukaFlow Access Token Test Business",
                        "ACCESS-TOKEN-TEST-BIZ",
                        null,
                        "access-token-business@dukaflow.test",
                        null,
                        "KES"));

        Role role = roleRepository.save(
                new Role(
                        business,
                        "BUSINESS_OWNER",
                        "Test business owner",
                        true));

        User user = userRepository.save(
                new User(
                        business,
                        role,
                        "Access Token Test User",
                        "access-token-user@dukaflow.test",
                        "accesstokenuser",
                        passwordEncoder.encode("TestPassword123!"),
                        null));

        DukaFlowUserDetails userDetails = new DukaFlowUserDetails(
                user.getId(),
                business.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getUsername(),
                user.getPasswordHash(),
                role.getName(),
                Set.of(),
                user.isActive());

        String accessToken = jwtTokenService.generateAccessToken(userDetails);

        mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "refreshToken": "%s"
                        }
                        """.formatted(accessToken)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code")
                        .value("AUTHENTICATION_REQUIRED"));
    }
}
