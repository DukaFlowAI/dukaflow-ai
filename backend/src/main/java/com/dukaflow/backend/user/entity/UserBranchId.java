package com.dukaflow.backend.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserBranchId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "branch_id")
    private Long branchId;

    public UserBranchId() {
    }

    public UserBranchId(Long userId, Long branchId) {
        this.userId = userId;
        this.branchId = branchId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof UserBranchId that)) {
            return false;
        }

        return Objects.equals(userId, that.userId)
                && Objects.equals(branchId, that.branchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, branchId);
    }
}
