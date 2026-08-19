package com.dukaflow.backend.user.repository;

import com.dukaflow.backend.user.entity.UserBranch;
import com.dukaflow.backend.user.entity.UserBranchId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBranchRepository
        extends JpaRepository<UserBranch, UserBranchId> {

    List<UserBranch> findByUser_Id(Long userId);

    List<UserBranch> findByBranch_Id(Long branchId);

    boolean existsByUser_IdAndBranch_Id(
            Long userId,
            Long branchId);

    void deleteByUser_IdAndBranch_Id(
            Long userId,
            Long branchId);
}
