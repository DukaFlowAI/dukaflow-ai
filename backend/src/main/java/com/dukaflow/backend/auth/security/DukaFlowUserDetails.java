package com.dukaflow.backend.auth.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class DukaFlowUserDetails implements UserDetails {

    private final Long userId;
    private final Long businessId;
    private final String fullName;
    private final String email;
    private final String username;
    private final String password;
    private final String roleName;
    private final Set<Long> branchIds;
    private final boolean active;

    public DukaFlowUserDetails(
            Long userId,
            Long businessId,
            String fullName,
            String email,
            String username,
            String password,
            String roleName,
            Set<Long> branchIds,
            boolean active) {
        this.userId = userId;
        this.businessId = businessId;
        this.fullName = fullName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roleName = roleName;
        this.branchIds = Set.copyOf(branchIds);
        this.active = active;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getRoleName() {
        return roleName;
    }

    public Set<Long> getBranchIds() {
        return branchIds;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + roleName));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        if (username != null && !username.isBlank()) {
            return username;
        }

        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
