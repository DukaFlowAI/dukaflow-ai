package com.dukaflow.backend.branch.repository;

import com.dukaflow.backend.branch.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    List<Branch> findByBusinessId(Long businessId);

    List<Branch> findByBusinessIdAndActiveTrue(Long businessId);

    Optional<Branch> findByBusinessIdAndBranchCode(
            Long businessId,
            String branchCode);

    boolean existsByBusinessIdAndBranchCode(
            Long businessId,
            String branchCode);
}
