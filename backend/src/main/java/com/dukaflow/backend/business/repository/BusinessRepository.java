package com.dukaflow.backend.business.repository;

import com.dukaflow.backend.business.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    Optional<Business> findByBusinessCode(String businessCode);

    boolean existsByBusinessCode(String businessCode);
}
