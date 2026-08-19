package com.dukaflow.backend.role.repository;

import com.dukaflow.backend.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByBusinessId(Long businessId);

    List<Role> findByBusinessIdAndActiveTrue(Long businessId);

    Optional<Role> findByBusinessIdAndName(
            Long businessId,
            String name);

    boolean existsByBusinessIdAndName(
            Long businessId,
            String name);
}
