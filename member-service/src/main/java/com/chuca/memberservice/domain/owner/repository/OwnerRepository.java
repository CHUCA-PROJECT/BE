package com.chuca.memberservice.domain.owner.repository;

import com.chuca.memberservice.domain.owner.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByBusinessNum(String businessNum);
}
