package com.dukaflow.backend.user.repository;

import com.dukaflow.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByUsernameIgnoreCase(String username);

    Optional<User> findByBusinessIdAndEmailIgnoreCase(
            Long businessId,
            String email);

    List<User> findByBusinessId(Long businessId);

    List<User> findByBusinessIdAndActiveTrue(Long businessId);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByUsernameIgnoreCase(String username);
}
