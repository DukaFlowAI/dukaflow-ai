package com.dukaflow.backend.auth.security;

import com.dukaflow.backend.user.entity.User;
import com.dukaflow.backend.user.repository.UserBranchRepository;
import com.dukaflow.backend.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DukaFlowUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserBranchRepository userBranchRepository;

    public DukaFlowUserDetailsService(
            UserRepository userRepository,
            UserBranchRepository userBranchRepository) {
        this.userRepository = userRepository;
        this.userBranchRepository = userBranchRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String identifier)
            throws UsernameNotFoundException {

        User user = userRepository
                .findByEmailIgnoreCase(identifier)
                .or(() -> userRepository.findByUsernameIgnoreCase(identifier))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Invalid email, username or password."));

        Set<Long> branchIds = userBranchRepository
                .findByUser_Id(user.getId())
                .stream()
                .map(userBranch -> userBranch.getBranch().getId())
                .collect(Collectors.toSet());

        return new DukaFlowUserDetails(
                user.getId(),
                user.getBusiness().getId(),
                user.getFullName(),
                user.getEmail(),
                user.getUsername(),
                user.getPasswordHash(),
                user.getRole().getName(),
                branchIds,
                user.isActive());
    }
}
