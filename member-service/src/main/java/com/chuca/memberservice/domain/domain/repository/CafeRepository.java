package com.chuca.memberservice.domain.domain.repository;

import com.chuca.memberservice.domain.domain.entity.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long> {
}
